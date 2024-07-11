package com.conner.assistant.services;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import com.conner.assistant.dto.LoginResponseDTO;
import com.conner.assistant.models.ApplicationUser;
import com.conner.assistant.models.RefreshToken;
import com.conner.assistant.models.Role;
import com.conner.assistant.repository.RoleRepository;
import com.conner.assistant.repository.UserRepository;
import com.conner.assistant.utils.CookieUtility;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private CookieUtility cookieUtils;

    /**
     * Registers a new user with the given username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the registered ApplicationUser object
     */
    public ApplicationUser registerUser(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = fetchRole("USER");
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        return userRepository.save(new ApplicationUser(0L, username, encodedPassword, authorities));
    }

    /**
     * Logs in the user with the given username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param response the HttpServletResponse object used to set cookies
     * @return a ResponseEntity object with the login response or an Unauthorized status if authentication fails
     */
    public ResponseEntity<LoginResponseDTO> loginUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication auth = authenticateUser(username, password);
            String jwt = tokenService.generateJwt(auth);
            //basic remember me, not yet represented in frontend. split up method
            if (request.getCookies() != null){
                Cookie[] cookies = request.getCookies();
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")){
                        String value = cookie.getValue();
                        refreshTokenService.verifyExpiration(refreshTokenService.findByToken(value).get());
                    }
                }
                }else{
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);
                response.addCookie(cookieUtils.refreshTokenCookie(refreshToken.getToken()));
            }
            LoginResponseDTO login = new LoginResponseDTO(userRepository.findByUsername(username).get(), jwt);
            response.addCookie(cookieUtils.jwtCookie(jwt));
            return ResponseEntity.ok(login);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private Role fetchRole(String authority) {
        Optional<Role> role = roleRepository.findByAuthority(authority);
        return role.orElseThrow(() -> new IllegalArgumentException("Role not found: " + authority));
    }


}
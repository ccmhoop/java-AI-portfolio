package com.conner.assistant.services;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import com.conner.assistant.dto.LoginResponseDTO;
import com.conner.assistant.models.ApplicationUser;
import com.conner.assistant.models.RefreshToken;
import com.conner.assistant.models.Role;
import com.conner.assistant.repository.RefreshTokenRepository;
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
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
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
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private CookieUtility cookieUtility;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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
     * TODO Split up in the separate Login Methods based on Refresh Token or Manual Login
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
            String jwt = jwtService.generateJwt(auth);


            //basic remember me Method. Todo optimize and separate into its own method
            String refreshTokenValue = cookieUtility.getCookieValue(request, "refreshToken");
            RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenValue);

            ApplicationUser applicationUser = userRepository.findByUsername(username).orElseThrow();

            if (refreshToken == null || !refreshToken.getApplicationUser().getUsername().equals(username)) {
                refreshToken = refreshTokenRepository.findByApplicationUser(applicationUser).orElse(null);
                if (refreshToken == null) {
                    refreshToken = refreshTokenService.createRefreshToken(username);
                }
            }
            response.addCookie(cookieUtility.refreshTokenCookie(refreshToken.getToken()));

            LoginResponseDTO login = new LoginResponseDTO(userRepository.findByUsername(username).orElseThrow(), jwt);
            response.addCookie(cookieUtility.jwtCookie(jwt));
            return ResponseEntity.ok(login);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Verifies the access token and refresh token in the request.
     *
     * @param request the HttpServletRequest object containing the cookies
     * @return true if the access token and refresh token are valid and belong to the same user; false otherwise
     */
    public boolean verifyTokens(HttpServletRequest request) {
        String refreshToken = cookieUtility.getCookieValue(request,"refreshToken");
        String jwt = cookieUtility.getCookieValue(request,"accessToken");
        return jwtService.JwtVerifyUser(jwt).equals(refreshTokenService.refreshTokenVerifyUser(refreshToken));
    }

    private Authentication authenticateUser(String username, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private Role fetchRole(String authority) {
        Optional<Role> role = roleRepository.findByAuthority(authority);
        return role.orElseThrow(() -> new IllegalArgumentException("Role not found: " + authority));
    }

}
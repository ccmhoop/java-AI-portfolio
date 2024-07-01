package com.conner.assistant.controllers;

import com.conner.assistant.dto.LoginDTO;
import com.conner.assistant.dto.LoginResponseDTO;
import com.conner.assistant.dto.RegistrationDTO;
import com.conner.assistant.models.ApplicationUser;
import com.conner.assistant.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

//    //TODO jwt refresh
    @GetMapping ("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO body, HttpServletRequest request, HttpServletResponse response) {

        LoginResponseDTO loginResponse = authenticationService.loginUser(body.getUsername(), body.getPassword());

        if (loginResponse.getUser() == null || loginResponse.getJwt() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        response.addCookie(authenticationService.httpOnlyCookieJwt(loginResponse.getJwt()));

        CsrfToken csrfToken = csrfTokenRepository.generateToken(request);
        csrfTokenRepository.saveToken(csrfToken, request, response);

        return ResponseEntity.ok()
                .header("X-CSRF-Token", csrfToken.getToken())
                .body(loginResponse);
    }

}



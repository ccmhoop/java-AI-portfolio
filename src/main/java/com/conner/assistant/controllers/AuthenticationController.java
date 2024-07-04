package com.conner.assistant.controllers;

import com.conner.assistant.dto.LoginRequestDTO;
import com.conner.assistant.dto.LoginResponseDTO;
import com.conner.assistant.dto.RegistrationDTO;
import com.conner.assistant.models.ApplicationUser;
import com.conner.assistant.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {

        LoginResponseDTO loginResponse = authenticationService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (loginResponse.getUser() != null) {
            response.addCookie(authenticationService.httpOnlyCookieJwt(loginResponse.getJwt()));

            Cookie username = new Cookie("username", loginRequest.getUsername());
            username.setHttpOnly(true);
            username.setSecure(true);
            username.setPath("/");
            response.addCookie(username);

            return ResponseEntity.ok(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}





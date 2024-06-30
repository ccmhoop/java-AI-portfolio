package com.conner.assistant.controllers;

import com.conner.assistant.dto.LoginResponseDTO;
import com.conner.assistant.dto.RegistrationDTO;
import com.conner.assistant.models.ApplicationUser;
import com.conner.assistant.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationDTO body) {
        return authenticationService.registerUser(body.getUsername(), body.getPassword());
    }

    //TODO jwt refresh
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody RegistrationDTO body, HttpServletResponse response) {
        LoginResponseDTO loginResponse = authenticationService.loginUser(body.getUsername(), body.getPassword());

        if (loginResponse.getUser() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
        response.addCookie(authenticationService.httpOnlyCookieJwt(loginResponse.getJwt()));
        return ResponseEntity.ok(loginResponse);
    }

}

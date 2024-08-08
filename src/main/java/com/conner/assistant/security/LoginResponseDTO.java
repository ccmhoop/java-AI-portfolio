package com.conner.assistant.security;

import com.conner.assistant.applicationUser.ApplicationUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponseDTO {

    private ApplicationUser user;
    private String jwt;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(ApplicationUser user, String jwt ) {
        this.user = user;
        this.jwt = jwt;
    }

}
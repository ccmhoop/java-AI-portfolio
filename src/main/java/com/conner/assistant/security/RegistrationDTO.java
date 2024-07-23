package com.conner.assistant.security;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationDTO {

    private String username;
    private String password;

    public RegistrationDTO(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Registration info: user" + this.username + " password: " + this.password;
    }

}
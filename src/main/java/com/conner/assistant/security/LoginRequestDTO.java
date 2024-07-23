package com.conner.assistant.security;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDTO {

    private String username;
    private String password;

}
package com.conner.assistant.security.refreshToken;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.Instant;

public class RefreshTokenResolver {

    @Getter
    private final String username;

    private final RefreshToken refreshToken;

    public RefreshTokenResolver(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
        this.username = getRefreshTokenUsername();
    }

    private String getRefreshTokenUsername() {
        if (refreshToken != null && verifyRefreshToken()) {
            return refreshToken.getApplicationUser().getUsername();
        }
        throw new BadCredentialsException("token");
    }

    private boolean verifyRefreshToken() {
        return refreshToken.getExpiryDate().compareTo(Instant.now()) > 0;
    }

}

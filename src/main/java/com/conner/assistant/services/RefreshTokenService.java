package com.conner.assistant.services;

import com.conner.assistant.models.RefreshToken;
import com.conner.assistant.repository.RefreshTokenRepository;
import com.conner.assistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .applicationUser(userRepository.findByUsername(username).orElseThrow())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(60*60))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String refreshToken){
        return refreshTokenRepository.findByToken(refreshToken);
    }

    public String refreshTokenVerifyUser(String refreshToken){
        RefreshToken token = verifyRefreshToken(refreshToken);
        return token.getApplicationUser().getUsername();
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken token = findByToken(refreshToken).orElse(null);
        if(token != null && token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token is expired or not valid. Please make a new login..!");
        }
        return token;
    }

}
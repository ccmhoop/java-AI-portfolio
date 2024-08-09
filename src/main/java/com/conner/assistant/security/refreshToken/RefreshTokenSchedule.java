package com.conner.assistant.security.refreshToken;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
public class RefreshTokenSchedule {

     @Autowired
     private RefreshTokenRepository refreshTokenRepository;

    /**
     * This method is used to print and delete the refresh tokens that have expired.
     * The refresh tokens are fetched from the RefreshTokenRepository using the method findByExpiryDateBefore().
     * The method then deletes all the fetched refresh tokens using the deleteAll() method of the RefreshTokenRepository.
     * Finally, a log entry is created to indicate the number of refresh tokens deleted.
     */
    @Scheduled(fixedDelay = 1000*60*60)
     public void deleteExpiredRefreshTokens() {
        final List<RefreshToken> refreshTokens = refreshTokenRepository.findByExpiryDateBefore(Instant.now());
         refreshTokenRepository.deleteAll(refreshTokens);
        log.info("Refresh tokens deleted: {}", refreshTokens);
     }

}

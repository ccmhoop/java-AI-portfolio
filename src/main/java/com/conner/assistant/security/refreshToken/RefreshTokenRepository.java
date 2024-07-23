package com.conner.assistant.security.refreshToken;

import com.conner.assistant.applicationUser.ApplicationUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByApplicationUser(ApplicationUser applicationUser);

    List<RefreshToken> findByExpiryDateBefore(Instant expiryDate);
}

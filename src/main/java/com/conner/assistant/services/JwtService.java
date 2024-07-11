package com.conner.assistant.services;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import com.conner.assistant.utils.RSAKeyProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    private final RSAPublicKey publicKey;

    @Autowired
    public JwtService(RSAKeyProperties rsaKeyProperties) {
        this.publicKey = rsaKeyProperties.getPublicKey();
    }

    /**
     * Generates a JSON Web Token (JWT) using the provided authentication information.
     *
     * @param auth the authentication details
     * @return the generated JWT
     */
    public String generateJwt(Authentication auth) {

        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(auth.getName())
                .claim("roles", scope)
                .expiresAt(now.plusMillis(15000))
                .build();
        System.out.println(claims);
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    /**
     * TODO Add Proper return types
     * Verifies the validity of a JSON Web Token (JWT) with the provided token.
     *
     * @param token the JWT token to verify
     * @return The username if the token is valid and not expired; null otherwise
     */
    public String JwtVerifyUser(String token) {
        try {
            Instant now = Instant.now();
            // Parse token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Creates RSA verifier
            RSASSAVerifier verifier = new RSASSAVerifier(publicKey);

            // Verifies token
            if (!signedJWT.verify(verifier)) {
                System.out.println("JWT verification failed");
                throw new JwtException("JWT verification failed");
            }

            // Retrieves JWTClaimSet
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            //Checks Expiration Time
            if (claims.getExpirationTime().before(Date.from(now))) {
                System.out.println("JWT expired");
                throw new JwtException("JWT expired");
            }

            return claims.getStringClaim("sub");
        } catch (ParseException | JOSEException  | JwtException e) {
            return null;
        }
    }

}
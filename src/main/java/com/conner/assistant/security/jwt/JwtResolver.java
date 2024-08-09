package com.conner.assistant.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

public class JwtResolver {

    private final RSAPublicKey publicKey;

    @Getter
    private final String username;

    public JwtResolver(RSAPublicKey publicKey, String accessToken) {
        this.publicKey = publicKey;
        this.username = getJwtUser(accessToken);
    }

    private String getJwtUser(String accessToken) {
        try {
            // Parse token
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            // Creates RSA verifier
            jwtVerifyToken(signedJWT);
            // Retrieves JWTClaimSet
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            //Checks Expiration Time
            jwtVerifyExpiration(claims);
            //Returns username
            return claims.getStringClaim("sub");
        } catch (ParseException | JOSEException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void jwtVerifyExpiration(JWTClaimsSet claims) throws JOSEException {
        if (claims.getExpirationTime().before(Date.from(Instant.now()))) {
            throw new JOSEException("JWT expired");
        }
    }

    private void jwtVerifyToken(SignedJWT signedJWT) throws JOSEException {
        RSASSAVerifier verifier = new RSASSAVerifier(publicKey);
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("JWT verification failed");
        }
    }

}

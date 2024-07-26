package com.conner.assistant.security;


import com.conner.assistant.applicationUser.ApplicationUser;
import com.conner.assistant.security.jwt.JwtService;
import com.conner.assistant.security.jwt.RSAKeyProperties;
import com.conner.assistant.security.refreshToken.RefreshToken;
import com.conner.assistant.security.refreshToken.RefreshTokenRepository;
import com.conner.assistant.security.refreshToken.RefreshTokenService;
import com.conner.assistant.utils.Result;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;


@Component
public class HttpOnlyBearerTokenResolver implements BearerTokenResolver {


    private final RefreshTokenService refreshTokenService;

    private final RSAPublicKey publicKey;

    private final Instant now = Instant.now();

    @Autowired
    public HttpOnlyBearerTokenResolver(RSAKeyProperties rsaKeyProperties, RefreshTokenService refreshTokenService) {
        this.publicKey = rsaKeyProperties.getPublicKey();
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * needs to be optimized and improved (method fails on server restart because of request.getCookies)
     * JWT is Expired On restart and should be refreshed if the refreshToken is not expired/invalid user
     */
    @Override
    public String resolve(HttpServletRequest request) {
        if (request.getCookies() != null) {
            System.out.println("hallo");
            if (jwtVerifyUser(request).equals(refreshTokenUser(request))) {
                return WebUtils.getCookie(request, "accessToken").getValue();
            }
        }
        return new DefaultBearerTokenResolver().resolve(request);
    }

    private String refreshTokenUser(HttpServletRequest request) {
        String refreshTokenKey = Objects.requireNonNull(WebUtils.getCookie(request, "refreshToken")).getValue();
        if (!refreshTokenKey.isEmpty()) {
            RefreshToken token = refreshTokenService.verifyRefreshToken(refreshTokenKey);
            return token.getApplicationUser().getUsername();
        }
        throw new RuntimeException("refresh token not found");
    }

    private String jwtVerifyUser(HttpServletRequest request) {
        String token = Objects.requireNonNull(WebUtils.getCookie(request, "accessToken")).getValue();
        try {
            // Parse token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Creates RSA verifier
            jwtVerifyToken(signedJWT);

            // Retrieves JWTClaimSet
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            //Checks Expiration Time
            jwtVerifyExpiration(claims);

            return claims.getStringClaim("sub");
        } catch (ParseException | JOSEException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void jwtVerifyExpiration(JWTClaimsSet claims) throws JOSEException {
        if (claims.getExpirationTime().before(Date.from(now))) {
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
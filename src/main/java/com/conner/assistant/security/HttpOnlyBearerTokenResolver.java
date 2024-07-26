package com.conner.assistant.security;

import com.conner.assistant.security.jwt.RSAKeyProperties;
import com.conner.assistant.security.refreshToken.RefreshTokenService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Component
public class HttpOnlyBearerTokenResolver implements BearerTokenResolver {

    private final RefreshTokenService refreshTokenService;
    private final RSAPublicKey publicKey;
    private final Instant now = Instant.now();

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
        String accessToken = getCookieValue(request, "accessToken");
        String refreshToken = getCookieValue(request, "refreshToken");
        if (accessToken != null && refreshToken != null) {
            return resolveTokens(accessToken, refreshToken);
        }
        return new DefaultBearerTokenResolver().resolve(request);
    }

    //TODO add separate Refresh token Logic
    private String resolveTokens(String accessToken, String refreshToken) {
        if (Objects.equals(getJwtUser(accessToken), getRefreshTokenUser(refreshToken))) {
            return accessToken;
        }
        throw new BadCredentialsException("Invalid Credentials");
    }

    //Duplicate method in CookieUtility
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(cookieName))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }

    private String getRefreshTokenUser(String refreshToken) {
        return refreshTokenService.verifyRefreshToken(refreshToken).getApplicationUser().getUsername();
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
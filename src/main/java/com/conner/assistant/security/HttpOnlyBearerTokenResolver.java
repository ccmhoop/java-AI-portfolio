package com.conner.assistant.security;

import com.conner.assistant.security.jwt.JwtResolver;
import com.conner.assistant.security.jwt.RSAKeyProperties;
import com.conner.assistant.security.refreshToken.RefreshToken;
import com.conner.assistant.security.refreshToken.RefreshTokenResolver;
import com.conner.assistant.security.refreshToken.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Objects;

@Component
public class HttpOnlyBearerTokenResolver implements BearerTokenResolver {

    private final RefreshTokenService refreshTokenService;
    private final RSAPublicKey publicKey;

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
    private String resolveTokens(String accessToken, String refreshTokenKey) {
        JwtResolver jwtResolver = new JwtResolver(publicKey, accessToken);

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenKey).orElse(null);
        RefreshTokenResolver refreshTokenResolver = new RefreshTokenResolver(refreshToken);

        if (Objects.equals(jwtResolver.getUsername(), refreshTokenResolver.getUsername())) {
            return accessToken;
        }

        throw new BadCredentialsException("Invalid Credentials");
    }

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

}
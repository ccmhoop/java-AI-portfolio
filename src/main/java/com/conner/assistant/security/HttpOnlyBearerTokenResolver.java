package com.conner.assistant.security;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Objects;


@Component
public class HttpOnlyBearerTokenResolver implements BearerTokenResolver {


    /**
     needs to be optimized and improved (method fails on server restart because of request.getCookies)
     JWT is Expired On restart and should be refreshed if the refreshToken is not expired/invalid user
     */
    @Override
    public String resolve(HttpServletRequest request) {
        if (request.getCookies() != null) {
            String accessToken = Objects.requireNonNull(WebUtils.getCookie(request, "accessToken")).getValue();
            //Add more logic
//            System.out.println(jwtService.JwtVerifyUser(accessToken));

            return accessToken;
        }

        return new DefaultBearerTokenResolver().resolve(request);
    }

}
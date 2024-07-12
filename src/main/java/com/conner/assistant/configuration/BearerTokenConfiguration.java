package com.conner.assistant.configuration;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.web.util.WebUtils;

import java.util.Objects;


@Configuration
public class BearerTokenConfiguration implements BearerTokenResolver {

    /**
     needs to be optimized and improved (method fails on server restart because of request.getCookies)
     JWT is Expired On restart and should be refreshed if the refreshToken is not expired/invalid user
     */
    @Override
    public String resolve(HttpServletRequest request) {

        if (request.getCookies() != null) {
            System.out.println(request.getContextPath());
            return Objects.requireNonNull(WebUtils.getCookie(request, "accessToken")).getValue();
        }

        return null;
    }

}
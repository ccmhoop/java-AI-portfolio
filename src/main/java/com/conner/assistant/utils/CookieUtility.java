package com.conner.assistant.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtility {

    //Todo separate into refresh and jwt cookie. match expiring dates
    public Cookie jwtCookie(String value) {
        Cookie cookie = new Cookie("accessToken", value);
        cookie.setAttribute("SameSite","Strict");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60*30);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie refreshTokenCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setAttribute("SameSite","Strict");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60*60);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * TODO Error Handling when cookie == null "shouldn't be triggered if not logged in"
     * Returns the value of the specified cookie name from the given HttpServletRequest.
     *
     * @param request the HttpServletRequest object containing the cookies
     * @param cookieName the name of the cookie to retrieve
     * @return the value of the specified cookie, or null if the cookie does not exist
     */
    public String getCookieValue(HttpServletRequest request, String cookieName) {
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
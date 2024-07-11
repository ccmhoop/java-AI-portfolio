package com.conner.assistant.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtility {
    //Todo separate into refresh and jwt cookie. match expiring dates
    public Cookie jwtCookie(String value) {
        Cookie cookie = new Cookie("accessToken", value);
        cookie.setAttribute("SameSite","Strict");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(30);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie refreshTokenCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setAttribute("SameSite","Strict");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(30);
        cookie.setPath("/");
        return cookie;
    }



}

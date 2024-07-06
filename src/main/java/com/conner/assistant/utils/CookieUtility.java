package com.conner.assistant.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtility {

    public Cookie createCookie(String name, String value, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setAttribute("SameSite","Strict");
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(true);
        cookie.setMaxAge(60*30);
        cookie.setPath("/");
        return cookie;
    }

}

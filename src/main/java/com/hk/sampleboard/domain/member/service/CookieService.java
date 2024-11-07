package com.hk.sampleboard.domain.member.service;

import com.hk.sampleboard.global.constant.CookieConstant;
import com.hk.sampleboard.global.constant.TokenConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CookieService {

    public void setCookieForLogin(HttpServletResponse response, String accessToken){
        Cookie cookie = new Cookie(CookieConstant.TOKEN_COOKIE_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(TokenConstant.ACCESS_TOKEN_VALID_TIME));
        response.addCookie(cookie);
        log.info("setCookie : {}",cookie);
    }

    public void expireCookieForLogout(HttpServletResponse response){
        Cookie cookie = new Cookie(CookieConstant.TOKEN_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        log.info("setCookie : {}", cookie);
    }
}

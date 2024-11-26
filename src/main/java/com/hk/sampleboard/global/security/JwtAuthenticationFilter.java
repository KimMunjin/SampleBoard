package com.hk.sampleboard.global.security;

import com.hk.sampleboard.global.redis.token.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_HEADER = "Authorization";

    private final TokenProvider tokenProvider;
    private final SecurityService securityService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = this.resolveTokenFromRequest(request);

        if(StringUtils.hasText(token)) {
            //토큰이 blacklist에 있는지 확인
            String jti = tokenProvider.getTokenJti(token);
            if(tokenRepository.existsBlackListAccessToken(jti)) {
                //blacklist에 있으면 로그아웃 된 토큰이니 인증 거부
                filterChain.doFilter(request, response); //필터 체인에서 다음 필터를 호출하거나, 마지막 필터인 경우 대상 서블릿 호출
                return;
            }
            //유효한 토큰이면 인증 처리
            if(StringUtils.hasText(token)&&this.tokenProvider.validateToken(token)) {
                String email = tokenProvider.getEmail(token);
                Authentication auth = securityService.getAuthentication(email);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if(!ObjectUtils.isEmpty(token)&&token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}


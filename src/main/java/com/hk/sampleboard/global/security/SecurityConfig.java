package com.hk.sampleboard.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(httpBasic->httpBasic.disable())
                .csrf(csrf->csrf.disable()) //사용자가 csrf 토큰을 제출하면 서버는 제출된 csrf 토큰을 검증하여 신뢰성 확인
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //session 방식이 아닌 restApi 방식이기 때문에 STATELESS
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers("/member/regist").permitAll()
                                .requestMatchers("/member/login").permitAll()
                                .requestMatchers("/member/login/reissue").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
                //권한 설정 hasRole()은 @EnableMethodSecurity를 통해 어노테이션으로 처리
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("/swagger-ui/**"));
    }
}

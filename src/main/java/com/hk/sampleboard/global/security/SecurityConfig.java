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
        return http.httpBasic(httpBasic->httpBasic.disable())//기본 HTTP 인증 비활성화
                .csrf(csrf->csrf.disable()) //사용자가 csrf 토큰을 제출하면 서버는 제출된 csrf 토큰을 검증하여 신뢰성 확인(CSRF 보호 기능은 REST API에서 일반적으로 비활성화)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //session 방식이 아닌 restApi 방식이기 때문에 STATELESS
                .authorizeHttpRequests(auth->
                        auth
                                .requestMatchers("/member/regist").permitAll()
                                .requestMatchers("/member/login").permitAll()
                                .requestMatchers("/member/login/reissue").permitAll()
                                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //jwt 인증 필터를 UsernamePasswordAuthenticationFilter 이전에 추가
                .build();
                //권한 설정 hasRole()은 @EnableMethodSecurity를 통해 어노테이션으로 처리
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring()
                .requestMatchers("/swagger-ui/**") //Swagger 관련 문서 보안검사 제외
                .requestMatchers("/v3/api-docs/**")); //Swagger 관련 문서 보안검사 제외
    }
}

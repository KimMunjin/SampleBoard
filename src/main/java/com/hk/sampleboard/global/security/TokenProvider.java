package com.hk.sampleboard.global.security;

import com.hk.sampleboard.global.type.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final CustomUserDetailsService customUserDetailsService;

    private static final String KEY_ROLE = "role";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;//1 hour

    @Value("{spring.jwt.secret}")
    private String secret;


    /**
     * 토큰 생성(발급)
     * @param email
     * @param role
     * @return
     */
    public String generateToken(String email, Role role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLE, role.toString());

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //토큰 생성 시간
                .setExpiration(expiredDate) //토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512,this.secret) //사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(this.getEmail(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    /**
     * Claims 정보 파싱
     * @param token
     * @return
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }

    }
}

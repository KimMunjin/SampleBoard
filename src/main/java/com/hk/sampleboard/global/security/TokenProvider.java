package com.hk.sampleboard.global.security;

import com.hk.sampleboard.domain.member.dto.TokenDto;
import com.hk.sampleboard.domain.member.mapper.MemberMapper;
import com.hk.sampleboard.domain.member.vo.Member;
import com.hk.sampleboard.global.constant.TokenConstant;
import com.hk.sampleboard.global.exception.CustomJwtException;
import com.hk.sampleboard.global.exception.ErrorCode;
import com.hk.sampleboard.global.exception.MemberException;
import com.hk.sampleboard.global.redis.token.repository.TokenRepository;
import com.hk.sampleboard.global.type.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.nio.charset.StandardCharsets;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final MemberMapper memberMapper;
    private Key key;

    private final TokenRepository refreshTokenRepository;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    //PostConstruct는 의존성 주입이 끝나고 실행 보장되며 어플리케이션이 실행될 때 한번만 실행된다.
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 토큰 생성
     *
     * @param email
     * @param role
     * @param expireTime
     * @return
     */
    public String generateToken(String email, Role role, Long expireTime) {
        String tokenId = UUID.randomUUID().toString();//토큰 ID 생성

        //이메일을 토큰 주제로 하는 claim 생성
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(TokenConstant.KEY_ROLES, role.toString()); // KEY_ROLES 추가
        claims.put(TokenConstant.JTI_CLAIM_NAME, tokenId); // 토큰 ID 추가

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //토큰 생성 시간
                .setExpiration(expiredDate) //토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)//사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    /**
     * 토큰 재발급
     * 
     * @param refreshToken
     * @return
     */
    public TokenDto regenerateToken(String refreshToken) {
        //refreshToken의 토큰 유효성 검증
        if(!validateToken(refreshToken)) {
            throw new CustomJwtException(ErrorCode.JWT_EXPIRED);
        }

        // refreshToken의 클레임을 파싱
        Claims claims = parseClaims(refreshToken);
        //email 값 얻기(getEmail 메서드를 만들었지만 claims가 필요하여 getEmail 메서드 사용하지 않고 진행)
        String email = claims.getSubject();
        


        //email 통한 member 검색
        Member member = memberMapper.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        //claims에서 문자열로 저장된 role 값을 가져와서 해당 Role enum 상수로 변환
        Role role = Role.valueOf(claims.get(TokenConstant.KEY_ROLES, String.class));
    
        //email 통해 redis에 있는 refreshToken 값 가져오기
        String findToken = refreshTokenRepository.getToken(email);

        //넘어온 refreshToken과 redis에 저장된 refreshToken이 다를 때
        if(!refreshToken.equals(findToken)) {
            throw new CustomJwtException(ErrorCode.JWT_REFRESH_TOKEN_NOT_FOUND);
        }

        return saveTokenInRedis(email,role,member.getMemberId());
    }

    public TokenDto saveTokenInRedis(String email, Role role, Long memberId) {
        String accessToken = generateToken(email, role, TokenConstant.ACCESS_TOKEN_VALID_TIME);

        String refreshToken = generateToken(email, role, TokenConstant.REFRESH_TOKEN_VALID_TIME);

        refreshTokenRepository.saveToken(refreshToken, email);

        return TokenDto.builder()
                .memberId(memberId)
                .tokenType(TokenConstant.BEARER)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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
     * 헤더로 받은 값에서 PREFIX("Bearer ") 제거
     */
    public String resolveTokenFromRequest(String token) {
        if (StringUtils.hasText(token) && token.startsWith(TokenConstant.BEARER)) {
            return token.substring(TokenConstant.BEARER.length());
        }
        return null;
    }

    // 토큰이 유효한지 확인하는 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // 파싱하는 과정에서 토큰 만료 시간이 지날 수 있다, 만료된 토큰을 확인할 때에
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(ErrorCode.JWT_EXPIRED);
            // 토큰 형식에 문제가 있을 때
        } catch (SignatureException e) {
            throw new CustomJwtException(ErrorCode.JWT_TOKEN_WRONG_TYPE);
            // 토큰이 변조되었을 때
        } catch (MalformedJwtException e) {
            throw new CustomJwtException(ErrorCode.JWT_TOKEN_MALFORMED);
        }
    }
    public String getTokenJti(String token) {
        return parseClaims(token).get(TokenConstant.JTI_CLAIM_NAME, String.class);
    }

}

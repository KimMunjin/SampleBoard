package com.hk.sampleboard.global.redis.token.repository;

import com.hk.sampleboard.global.constant.TokenConstant;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepository implements TokenRepository {

    private final RedisTemplate redisTemplate;

    @Override
    public void saveToken(String refreshToken, String email) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(TokenConstant.REFRESH_TOKEN_PREFIX + email,
                refreshToken,
                Duration.ofMillis(TokenConstant.REFRESH_TOKEN_VALID_TIME));
    }

    @Override
    public String getToken(String email) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(TokenConstant.REFRESH_TOKEN_PREFIX + email);
    }

    // 로그아웃
    @Override
    public boolean deleteToken(String email) {
        boolean result = redisTemplate.hasKey(TokenConstant.REFRESH_TOKEN_PREFIX + email);

        if (result) redisTemplate.delete(TokenConstant.REFRESH_TOKEN_PREFIX + email);

        return result;
    }

    /**
     * accessToken 블랙리스트 추가
     * @param jti
     */
    @Override
    public void addBlackListAccessToken(String jti) {
        try{
            ValueOperations<String, String> values = redisTemplate.opsForValue();
            values.set(TokenConstant.ACCESS_TOKEN_PREFIX + jti,
                    TokenConstant.BLACK_LIST,
                    Duration.ofMillis(TokenConstant.ACCESS_TOKEN_VALID_TIME));
        } catch (Exception e) {
            throw new JwtException("accessToekn 블랙리스트 추가 실패");
        }

    }

    /**
     * accessToken이 Blacklist에 남아있는지 여부 확인
     * 해당 accessToken은 사용하지 못 한다
     * @param jti
     */
    @Override
    public boolean existsBlackListAccessToken(String jti) {
        try {
            return redisTemplate.hasKey(TokenConstant.ACCESS_TOKEN_PREFIX+jti);
        }catch (Exception e) {
            return false;
        }

        //return redisTemplate.hasKey(TokenConstant.ACCESS_TOKEN_PREFIX + accessToken);
    }
}

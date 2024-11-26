package com.hk.sampleboard.global.redis.token.repository;

import com.hk.sampleboard.global.constant.TokenConstant;
import com.hk.sampleboard.global.exception.CustomJwtException;
import com.hk.sampleboard.global.exception.ErrorCode;
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

    /**
     * 토큰 저장
     * 
     * @param refreshToken
     * @param email
     */
    @Override
    public void saveToken(String refreshToken, String email) {
        //Spring Data Redis에 있는 ValueOperations 사용 -> 간단한 값 또는 문자열 관련 작업 처리
        ValueOperations<String, String> values = redisTemplate.opsForValue(); //opsForValue는 문자열 key-value 쌍 처리 위한 ValueOperations 반환
        values.set(TokenConstant.REFRESH_TOKEN_PREFIX + email,
                refreshToken,
                Duration.ofMillis(TokenConstant.REFRESH_TOKEN_VALID_TIME));//저장 지속 시간
    }

    /**
     * 저장된 Token 가져오기
     *
     * @param email
     * @return
     */
    @Override
    public String getToken(String email) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(TokenConstant.REFRESH_TOKEN_PREFIX + email);
    }

    /**
     * 로그아웃 시 redis에서 토큰 삭제
     * 
     * @param email
     * @return
     */
    @Override
    public boolean deleteToken(String email) {
        boolean result = redisTemplate.hasKey(TokenConstant.REFRESH_TOKEN_PREFIX + email);

        if (result) {
            //email로 된 key가 존재할 경우 delete
            redisTemplate.delete(TokenConstant.REFRESH_TOKEN_PREFIX + email);
        }
        return result;
    }

    /**
     * accessToken 블랙리스트 추가
     *
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
            throw new CustomJwtException(ErrorCode.JWT_BLACKLIST_FAILED);
        }
    }

    /**
     * accessToken이 Blacklist에 남아있는지 여부 확인
     * 해당 accessToken은 사용하지 못 한다
     *
     * @param jti
     */
    @Override
    public boolean existsBlackListAccessToken(String jti) {
        try {
            return redisTemplate.hasKey(TokenConstant.ACCESS_TOKEN_PREFIX+jti);
        }catch (Exception e) {
            return false;
        }

    }
}

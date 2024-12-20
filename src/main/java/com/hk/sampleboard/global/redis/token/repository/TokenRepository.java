package com.hk.sampleboard.global.redis.token.repository;

public interface TokenRepository {
    public void saveToken(String refreshToken, String email);

    public String getToken(String email);

    public boolean deleteToken(String email);

    public void addBlackListAccessToken(String jti);

    public boolean existsBlackListAccessToken(String jti);
}

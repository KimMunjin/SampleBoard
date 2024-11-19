package com.hk.sampleboard.domain.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
}

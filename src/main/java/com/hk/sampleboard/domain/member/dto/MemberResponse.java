package com.hk.sampleboard.domain.member.dto;

import com.hk.sampleboard.global.type.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {
    private Long memberId;
    private String email;
    private String nickname;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    public static MemberResponse fromDto(MemberDto member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .lastLoginAt(member.getLastLoginAt())
                .build();
    }
}

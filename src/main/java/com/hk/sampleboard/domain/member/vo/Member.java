package com.hk.sampleboard.domain.member.vo;

import com.hk.sampleboard.global.type.Role;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Long memberId;
    private String email;
    private String password;
    private String nickname;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
        this.updatedAt = LocalDateTime.now();
    }

}

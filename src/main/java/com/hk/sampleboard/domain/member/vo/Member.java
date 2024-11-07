package com.hk.sampleboard.domain.member.vo;

import com.hk.sampleboard.global.type.Role;
import lombok.*;


import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId) && Objects.equals(email, member.email) && Objects.equals(password, member.password) && Objects.equals(nickname, member.nickname) && role == member.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, email, password, nickname, role);
    }

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

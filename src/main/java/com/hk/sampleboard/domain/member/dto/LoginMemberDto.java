package com.hk.sampleboard.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginMemberDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        private String email;
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        private TokenDto tokenDto;
        private MemberResponse memberResponse;
    }
}

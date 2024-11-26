package com.hk.sampleboard.domain.member.dto;

import com.hk.sampleboard.domain.member.vo.Member;
import com.hk.sampleboard.global.annotation.Email;
import com.hk.sampleboard.global.constant.ResponseConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class UpdateMemberDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long memberId;

        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Email(message = "유효한 이메일 주소를 입력하세요.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        private String password;

        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        private String nickname;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long memberId;
        private String email;
        private String nickname;
        private String message;

        public static Response from(MemberDto member) {
            return Response.builder()
                    .memberId(member.getMemberId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .message(ResponseConstant.UPDATE_MEMBER_SUCCESS)
                    .build();
        }
    }
}

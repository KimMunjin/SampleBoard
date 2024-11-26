package com.hk.sampleboard.domain.member.dto;

import com.hk.sampleboard.domain.member.vo.Member;
import com.hk.sampleboard.global.annotation.Email;
import com.hk.sampleboard.global.constant.ResponseConstant;
import com.hk.sampleboard.global.type.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

public class RegistMemberDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = ResponseConstant.EMAIL_NECESSARY)
        @Email(message = ResponseConstant.EMIAL_NOT_VALID)
        private String email;

        @NotBlank(message = ResponseConstant.PASSWORD_NECESSARY)
        private String password;

        @NotBlank(message = ResponseConstant.NICKNAME_NECESSARY)
        private String nickname;

        public static Member toVo (RegistMemberDto.Request request) {
            return Member.builder()
                    .email(request.email)
                    .password(request.password)
                    .nickname(request.nickname)
                    .createdAt(LocalDateTime.now())
                    .role(Role.USER)
                    .build();
        }
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

        public static RegistMemberDto.Response createRegistResponse(MemberDto member) {
            return Response.builder()
                    .memberId(member.getMemberId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .message(ResponseConstant.JOIN_SUCCESS)
                    .build();
        }
    }

}

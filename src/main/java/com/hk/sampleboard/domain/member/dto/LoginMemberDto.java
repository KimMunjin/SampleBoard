package com.hk.sampleboard.domain.member.dto;

import com.hk.sampleboard.global.annotation.Email;
import com.hk.sampleboard.global.constant.ResponseConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginMemberDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{

        @NotBlank(message = ResponseConstant.EMAIL_NECESSARY)
        @Email(message = ResponseConstant.EMIAL_NOT_VALID)
        private String email;

        @NotBlank(message = ResponseConstant.PASSWORD_NECESSARY)
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

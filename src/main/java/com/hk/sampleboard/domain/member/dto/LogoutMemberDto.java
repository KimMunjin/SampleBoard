package com.hk.sampleboard.domain.member.dto;

import lombok.*;

public class LogoutMemberDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long memberId;
        private String token;
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long memberId;
        private String message;

    }
}

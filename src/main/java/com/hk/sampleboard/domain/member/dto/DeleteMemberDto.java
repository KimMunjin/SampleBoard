package com.hk.sampleboard.domain.member.dto;

import lombok.*;

public class DeleteMemberDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long memberId;
        private String password;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long memberId;
        private String email;
        private String message;
    }
}

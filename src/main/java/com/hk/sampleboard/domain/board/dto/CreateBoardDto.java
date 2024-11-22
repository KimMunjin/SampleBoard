package com.hk.sampleboard.domain.board.dto;


import com.hk.sampleboard.domain.board.vo.Board;
import com.hk.sampleboard.global.constant.ResponseConstant;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class CreateBoardDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long boardId;
        private Long memberId;
        private String title;
        private String message;

        public Response(BoardDto boardDto) {
            this.boardId = boardDto.getBoardId();
            this.memberId = boardDto.getMemberId();
            this.title = boardDto.getTitle();
            this.message = ResponseConstant.CREATE_BOARD;
        }
        public static BoardDto savedBoardIdMemberIdnTitle(Board board, Long boardId) {
            return BoardDto.builder()
                    .boardId(boardId)
                    .memberId(board.getMemberId())
                    .title(board.getTitle())
                    .build();
        }
    }
}

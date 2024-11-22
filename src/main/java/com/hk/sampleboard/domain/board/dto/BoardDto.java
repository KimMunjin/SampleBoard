package com.hk.sampleboard.domain.board.dto;

import com.hk.sampleboard.domain.board.vo.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long boardId;

    private Long memberId;

    private String nickname;

    private String title;

    private String content;

    private Long viewCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deleted;

    private LocalDateTime deletedAt;


}

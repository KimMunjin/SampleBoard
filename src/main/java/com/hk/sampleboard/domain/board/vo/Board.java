package com.hk.sampleboard.domain.board.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
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
    
    // 게시글 수정
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCount = this.viewCount + 1L;
    }

    // 게시글 삭제
    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    // 작성자 확인
    public boolean isWriter(Long memberId) {
        return this.memberId.equals(memberId);
    }
}

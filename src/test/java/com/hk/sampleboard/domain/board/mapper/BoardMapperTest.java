package com.hk.sampleboard.domain.board.mapper;

import com.hk.sampleboard.domain.board.vo.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardMapperTest {

    @Autowired
    private BoardMapper boardMapper;

    @Test
    void insertBoard() {

        Board board = Board.builder()
                .memberId(1L)
                .title("test")
                .content("test")
                .nickname("test")
                .build();
        Long boardId = boardMapper.insertBoard(board);
        System.out.println("boardId" + boardId);
    }
}
package com.hk.sampleboard.domain.board.service;

import com.hk.sampleboard.domain.board.dto.BoardDto;
import com.hk.sampleboard.domain.board.dto.CreateBoardDto;

public interface BoardService {

    BoardDto createBoard(CreateBoardDto.Request request, Long memberId);
}

package com.hk.sampleboard.domain.board.service.impl;

import com.hk.sampleboard.domain.board.dto.BoardDto;
import com.hk.sampleboard.domain.board.dto.CreateBoardDto;
import com.hk.sampleboard.domain.board.mapper.BoardMapper;
import com.hk.sampleboard.domain.board.service.BoardService;
import com.hk.sampleboard.domain.board.vo.Board;
import com.hk.sampleboard.domain.member.dto.MemberDto;
import com.hk.sampleboard.domain.member.mapper.MemberMapper;
import com.hk.sampleboard.domain.member.vo.Member;
import com.hk.sampleboard.global.exception.ErrorCode;
import com.hk.sampleboard.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final MemberMapper memberMapper;

    @Override
    public BoardDto createBoard(CreateBoardDto.Request request, Long memberId) {
        Member member = memberMapper.findById(memberId)
                .orElseThrow(()->new MemberException(ErrorCode.MEMBER_NOT_FOUNT));
        Board board = Board.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        Long boardId = boardMapper.insertBoard(board);
        return CreateBoardDto.Response.savedBoardIdMemberIdnTitle(board, boardId);
    }
}

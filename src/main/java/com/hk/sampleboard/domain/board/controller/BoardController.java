package com.hk.sampleboard.domain.board.controller;

import com.hk.sampleboard.domain.board.dto.BoardDto;
import com.hk.sampleboard.domain.board.dto.CreateBoardDto;
import com.hk.sampleboard.domain.board.service.BoardService;
import com.hk.sampleboard.domain.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // 게시물 등록
    @PostMapping
    @Operation(summary = "게시물 등록", description = "게시물을 작성하여 등록하는 기능")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<CreateBoardDto.Response> createBoard(
            @AuthenticationPrincipal MemberDto memberDto,
            @RequestBody CreateBoardDto.Request request) {
        BoardDto savedBoard = boardService.createBoard(request, memberDto.getMemberId());
        return ResponseEntity.ok(new CreateBoardDto.Response(savedBoard));
    }



}

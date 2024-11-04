package com.hk.sampleboard.domain.member.controller;

import com.hk.sampleboard.domain.member.dto.RegistMemberDTO;
import com.hk.sampleboard.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/regist")
    public ResponseEntity<RegistMemberDTO.Response> registMember(
            @RequestBody @Valid RegistMemberDTO.Request request
    ) {
        RegistMemberDTO.Response response = memberService.registMember(request);
        return ResponseEntity.ok(response);
    }


}

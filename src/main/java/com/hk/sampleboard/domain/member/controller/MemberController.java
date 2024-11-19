package com.hk.sampleboard.domain.member.controller;

import com.hk.sampleboard.domain.member.dto.*;
import com.hk.sampleboard.domain.member.service.CookieService;
import com.hk.sampleboard.domain.member.service.MemberService;
import com.hk.sampleboard.global.exception.ErrorCode;
import com.hk.sampleboard.global.exception.MemberException;
import com.hk.sampleboard.global.security.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final CookieService cookieService;

    /**
     * 회원가입
     * @param request
     * @return
     */
    @PostMapping("/regist")
    @Operation(summary = "회원 가입",description = "회원 가입 메서드")
    public ResponseEntity<RegistMemberDto.Response> registMember(
            @RequestBody @Valid RegistMemberDto.Request request
    ) {
        log.info("회원가입 요청 : email ={}",request.getEmail());
        RegistMemberDto.Response response = memberService.registMember(request);
        log.info("회원가입 완료 : email = {}",response.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인
     * @param request
     * @param httpServletResponse
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 후 토큰 발급")
    public ResponseEntity<LoginMemberDto.Response> loginMember(
            @RequestBody @Valid LoginMemberDto.Request request
            , HttpServletResponse httpServletResponse){
        log.info("로그인 요청 : email ={}",request.getEmail());
        MemberResponse memberResponse = memberService.loginMember(request);

        TokenDto tokenDto = tokenProvider.saveTokenInRedis(memberResponse.getEmail(),memberResponse.getRole(),memberResponse.getMemberId());
        cookieService.setCookieForLogin(httpServletResponse, tokenDto.getAccessToken());
        log.info("로그인 성공 : memberId ={}, email = {}",memberResponse.getMemberId(), memberResponse.getEmail());
        return ResponseEntity.ok(LoginMemberDto.Response.builder()
                        .memberResponse(memberResponse)
                        .tokenDto(tokenDto)
                        .build());
    }
    
    /**
     * 토큰 재발급
     * access token이 만료될 때에, refresh token을 확인하고, access token과 refresh token을 재발급해준다
     * @param refreshToken
     * @param response
     * @return
     */
    @PostMapping("/login/reissue")
    @Operation(summary = "Access token 재발급하기", description = "refresh token 확인 후, Access token 재발급하기")
    public ResponseEntity<TokenDto> reissueAccessToken(
            @RequestHeader("RefreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        refreshToken = tokenProvider.resolveTokenFromRequest(refreshToken);
        TokenDto tokenDto = tokenProvider.regenerateToken(refreshToken);
        cookieService.setCookieForLogin(response, tokenDto.getAccessToken());
        log.info("토큰 재발급 : memberId = {}",tokenDto.getMemberId());
        return ResponseEntity.ok(tokenDto);
    }

    /**
     * 로그아웃 http://localhost:8080/member/logout
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "refreshToken을 삭제하고, access token을 blackList로 돌린다")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<String> memberLogout(
            @AuthenticationPrincipal MemberDto memberDto,
            @RequestHeader("Authorization") String accessToken,
            HttpServletResponse httpServletResponse
    ) {
        log.info("로그아웃 요청 : memberId = {}", memberDto.getMemberId());
        String token = tokenProvider.resolveTokenFromRequest(accessToken);

        cookieService.expireCookieForLogout(httpServletResponse);
        log.info("로그아웃 완료 : memberId = {}", memberDto.getMemberId());
        return ResponseEntity.ok(memberService.logoutMember(token, memberDto.getEmail()));
    }

    /**
     * 회원 정보 수정
     * @param request
     * @param memberDto
     * @return
     */
    @PostMapping("/update")
    @Operation(summary = "회원 정보 수정", description = "닉네임과 비밀번호를 수정할 수 있습니다.")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UpdateMemberDto.Response> updateMember(
            @RequestBody @Valid UpdateMemberDto.Request request,
            @AuthenticationPrincipal MemberDto memberDto
    ) {
        // 본인 확인
        if (!memberDto.getMemberId().equals(request.getMemberId())) {
            throw new MemberException(ErrorCode.NO_AUTHORITY_ERROR);
        }

        return ResponseEntity.ok(memberService.updateMember(request));
    }

    @PostMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 처리를 합니다.")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<DeleteMemberDto.Response> deleteMember(
            @AuthenticationPrincipal MemberDto memberDto,
            @RequestHeader("Authorization") String accessToken,
            @RequestBody @Valid DeleteMemberDto.Request request
    ) {
        // 본인 확인
        if (!memberDto.getMemberId().equals(request.getMemberId())) {
            throw new MemberException(ErrorCode.NO_AUTHORITY_ERROR);
        }

        return ResponseEntity.ok(memberService.deleteMember(request, accessToken));
    }


}

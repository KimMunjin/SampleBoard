package com.hk.sampleboard.domain.member.service.impl;

import com.hk.sampleboard.domain.member.dto.LoginMemberDto;
import com.hk.sampleboard.domain.member.dto.MemberDto;
import com.hk.sampleboard.domain.member.dto.MemberResponse;
import com.hk.sampleboard.domain.member.dto.RegistMemberDto;
import com.hk.sampleboard.domain.member.mapper.MemberMapper;
import com.hk.sampleboard.domain.member.vo.Member;
import com.hk.sampleboard.global.constant.ResponseConstant;
import com.hk.sampleboard.global.exception.ErrorCode;
import com.hk.sampleboard.global.exception.MemberException;
import com.hk.sampleboard.global.security.SecurityService;
import com.hk.sampleboard.global.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityService securityService;
    @InjectMocks
    private MemberServiceImpl memberService;


    private static Member member(String email, String password, String nickname, Role role) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.USER)
                .build();
    }

    @Nested
    @DisplayName("회원가입")
    class registMember {
        @Test
        @DisplayName("회원가입 성공")
        void registMemberSuccess() {
            String email = "test@test.com";
            String password = "aaa1234";
            String encodedPassword = "encodedPassword";  // 인코딩된 비밀번호 값
            String nickname = "testNickname";
            // given
            RegistMemberDto.Request request = RegistMemberDto.Request.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build();

            Member member = member(request.getEmail(),encodedPassword, request.getNickname(),Role.USER);

            when(memberMapper.existEmail(anyString())).thenReturn(false);
            when(memberMapper.existNickname(anyString())).thenReturn(false);
            when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
            when(memberMapper.insertMember(any(Member.class))).thenReturn(member);

            // when
            RegistMemberDto.Response response = memberService.registMember(request);

            // then
            assertNotNull(response);
            assertThat(response.getEmail()).isEqualTo(member.getEmail());
            assertThat(response.getNickname()).isEqualTo(member.getNickname());
            assertEquals(ResponseConstant.JOIN_SUCCESS, response.getMessage());
            verify(memberMapper, times(1)).insertMember(member);
        }
        @Test
        @DisplayName("회원가입 실패 - 이메일 중복")
        void registMemberFailEmail() {
            String email = "test@test.com";
            String password = "aaa1234";
            String nickname = "testNickname";

            RegistMemberDto.Request request = RegistMemberDto.Request.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build();

            when(memberMapper.existEmail(email)).thenReturn(true);

            MemberException memberException = catchThrowableOfType(()->memberService.registMember(request),MemberException.class);

            assertEquals(ErrorCode.EMAIL_ALREADY_EXIST, memberException.getErrorCode());
            verify(memberMapper, never()).insertMember(any(Member.class));
        }
        @Test
        @DisplayName("회원가입 실패 - 닉네임 중복")
        void registMemberFailNickname() {
            String email = "test@test.com";
            String password = "aaa1234";
            String nickname = "testNickname";

            RegistMemberDto.Request request = RegistMemberDto.Request.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build();
            when(memberMapper.existEmail(email)).thenReturn(false);
            when(memberMapper.existNickname(nickname)).thenReturn(true);

            MemberException memberException = catchThrowableOfType(()->memberService.registMember(request),MemberException.class);

            assertEquals(ErrorCode.NICKNAME_ALREADY_EXIST, memberException.getErrorCode());
            verify(memberMapper, never()).insertMember(any(Member.class));
        }
    }
    @Nested
    @DisplayName("로그인")
    class loginMember {
        @Test
        @DisplayName("로그인 성공")
        void loginMemberSuccess() {
            // given
            String email = "test@test.com";
            String rawPassword = "password123";
            String encodedPassword = "encodedPassword";
            String nickname = "nickname";

            LoginMemberDto.Request request = LoginMemberDto.Request.builder()
                    .email(email)
                    .password(rawPassword)
                    .build();

            Member member = member(request.getEmail(), encodedPassword, nickname, Role.USER);
            MemberDto memberDto = MemberDto.fromVo(member);

            when(securityService.loadUserByUsername(email)).thenReturn(memberDto);
            when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
            when(memberMapper.findByEmail(email)).thenReturn(Optional.of(member));
            when(memberMapper.updateMember(any(Member.class))).thenReturn(member);

            // when
            MemberResponse response = memberService.loginMember(request);

            // then
            assertNotNull(response);
            assertEquals(email, response.getEmail());
            assertNotNull(member.getLastLoginAt());
            verify(memberMapper,times(1)).updateMember(member);
        }

        @Test
        @DisplayName("로그인 실패 - 존재하지 않는 이메일")
        void loginMemberFailEmailNotFound() {
            // given
            String email = "nonexistent@test.com";
            String password = "password123";

            LoginMemberDto.Request request = LoginMemberDto.Request.builder()
                    .email(email)
                    .password(password)
                    .build();

            when(securityService.loadUserByUsername(email))
                    .thenThrow(new UsernameNotFoundException("User not found"));

            // when & then
            MemberException memberException = catchThrowableOfType(()->memberService.loginMember(request),MemberException.class);

            assertEquals(ErrorCode.MEMBER_NOT_FOUND, memberException.getErrorCode());
            verify(memberMapper, never()).updateMember(any(Member.class));
        }

        @Test
        @DisplayName("로그인 실패 - 잘못된 비밀번호")
        void loginMemberFailInvalidPassword() {
            // given
            String email = "test@test.com";
            String wrongPassword = "wrongPassword";
            String encodedPassword = "encodedPassword";
            String nickname = "nickname";

            LoginMemberDto.Request request = LoginMemberDto.Request.builder()
                    .email(email)
                    .password(wrongPassword)
                    .build();

            Member member = member(email, encodedPassword, nickname, Role.USER);
            MemberDto memberDto = MemberDto.fromVo(member);


            when(securityService.loadUserByUsername(email)).thenReturn(memberDto);
            when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

            // when & then
            MemberException memberException = catchThrowableOfType(()->memberService.loginMember(request),MemberException.class);

            assertEquals(ErrorCode.INVALID_EMAIL_PASSWORD, memberException.getErrorCode());
            verify(memberMapper, never()).updateMember(any(Member.class));
        }

        @Test
        @DisplayName("로그인 실패 - Member 엔티티 조회 실패")
        void loginMemberFailMemberNotFound() {
            // given
            String email = "test@test.com";
            String password = "password123";
            String encodedPassword = "encodedPassword";
            String nickname = "nickname";

            LoginMemberDto.Request request = LoginMemberDto.Request.builder()
                    .email(email)
                    .password(password)
                    .build();

            Member member = member(email, encodedPassword, nickname, Role.USER);
            MemberDto memberDto = MemberDto.fromVo(member);

            when(securityService.loadUserByUsername(email)).thenReturn(memberDto);
            when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
            when(memberMapper.findByEmail(email)).thenReturn(Optional.empty());

            // when & then
            MemberException memberException = catchThrowableOfType(()->memberService.loginMember(request),MemberException.class);

            assertEquals(ErrorCode.MEMBER_NOT_FOUND, memberException.getErrorCode());
            verify(memberMapper, never()).updateMember(any(Member.class));
        }
    }
}
package com.hk.sampleboard.domain.member.service.impl;

import com.hk.sampleboard.domain.member.dto.RegistMemberDto;
import com.hk.sampleboard.domain.member.mapper.MemberMapper;
import com.hk.sampleboard.domain.member.vo.Member;
import com.hk.sampleboard.global.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
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

            Member member = member(request.getEmail(),encodedPassword,request.getNickname(),Role.USER);

            when(memberMapper.existEmail(anyString())).thenReturn(false);
            when(memberMapper.existNickname(anyString())).thenReturn(false);
            when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
            when(memberMapper.save(member)).thenReturn(1);

            // when
            RegistMemberDto.Response response = memberService.registMember(request);

            // then
            assertNotNull(response);
            assertThat(response.getEmail()).isEqualTo(member.getEmail());
            assertThat(response.getNickname()).isEqualTo(member.getNickname());
            assertEquals("회원가입성공", response.getMessage());
            verify(memberMapper, times(1)).save(any(Member.class));
        }
    }
}
package com.hk.sampleboard.domain.member.service.impl;

import com.hk.sampleboard.domain.member.dto.*;
import com.hk.sampleboard.domain.member.mapper.MemberMapper;
import com.hk.sampleboard.domain.member.service.MemberService;
import com.hk.sampleboard.domain.member.vo.Member;
import com.hk.sampleboard.global.constant.TokenConstant;
import com.hk.sampleboard.global.redis.token.repository.TokenRepository;
import com.hk.sampleboard.global.security.TokenProvider;
import com.hk.sampleboard.global.type.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    /**
     *
     * @param request
     * @return
     */
    @Override
    public RegistMemberDto.Response registMember(RegistMemberDto.Request request) {
        //중복 체크 메서드들(email, 닉네임)
        boolean existEmail = existEmail(request.getEmail());
        if(existEmail) {
            //임시 에러
            throw new RuntimeException("Email already exists");
        }

        boolean existNickname = existNickname(request.getNickname());
        if(existNickname) {
            throw new RuntimeException("Nickname already exists");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(Role.USER)
                .build();
        memberMapper.save(member);
        return RegistMemberDto.Response.createRegistResponse(member);
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    @Override
    public MemberResponse loginMember(LoginMemberDto.Request request) {
        MemberDto memberDTO = (MemberDto) userDetailsService.loadUserByUsername(request.getEmail());
        if(!passwordEncoder.matches(request.getPassword(), memberDTO.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return MemberResponse.fromDto(memberDTO);
    }

    /**
     * 로그아웃 ServiceImpl
     * @param accessToken
     * @param email
     * @return
     */
    @Override
    public String logoutMember(String accessToken, String email) {
        boolean result = tokenRepository.deleteToken(email);

        if (!result) {
            return TokenConstant.LOGOUT_NOT_SUCCESSFUL;
        }
        String resolvedToken = tokenProvider.resolveTokenFromRequest(accessToken);
        String jti = tokenProvider.getTokenJti(resolvedToken);  // 여기서 jti 추출
        tokenRepository.addBlackListAccessToken(jti);

        return TokenConstant.LOGOUT_SUCCESSFUL;
    }

    @Override
    public UpdateMemberDto.Response updateMember(UpdateMemberDto.Request request) {
        Member member = memberMapper.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 닉네임 중복 체크
        if (request.getNickname() != null && !request.getNickname().equals(member.getNickname())) {
            if (existNickname(request.getNickname())) {
                throw new RuntimeException("이미 사용중인 닉네임입니다.");
            }
            member.updateNickname(request.getNickname());
        }

        // 비밀번호 변경
        if (request.getPassword() != null) {
            member.updatePassword(passwordEncoder.encode(request.getPassword()));
        }

        memberMapper.update(member);
        return UpdateMemberDto.Response.from(member);
    }

    @Override
    public DeleteMemberDto.Response deleteMember(DeleteMemberDto.Request request, String accessToken) {
        // 회원 조회
        Member member = memberMapper.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // Redis에서 RefreshToken 삭제
        boolean result = tokenRepository.deleteToken(member.getEmail());

        // AccessToken BlackList 추가 처리는 로그아웃과 동일
        if (!result) {
            throw new RuntimeException(TokenConstant.LOGOUT_NOT_SUCCESSFUL);
        }
        String resolvedToken = tokenProvider.resolveTokenFromRequest(accessToken);
        String jti = tokenProvider.getTokenJti(resolvedToken);  // 여기서 jti 추출
        tokenRepository.addBlackListAccessToken(jti);
        DeleteMemberDto.Response response = DeleteMemberDto.Response.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .build();
        // 회원 삭제
        int deleteResult = memberMapper.delete(member.getMemberId());
        if(deleteResult>0) {
            response.setMessage("회원 탈퇴 완료");
            return response;
        } else {
            throw new RuntimeException("회원 탈퇴 실패");
        }
    }


    //이메일 존재 여부 확인 메서드
    public boolean existEmail (String email) {
        return memberMapper.existEmail(email);
    }

    public boolean existNickname (String nickname) {
        return memberMapper.existNickname(nickname);
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Member member = memberRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 이메일 입니다.: " + email));
//        return MemberDto.fromVo(member);
//    }
}

package com.hk.sampleboard.domain.member.service.impl;

import com.hk.sampleboard.domain.member.dto.RegistMemberDTO;
import com.hk.sampleboard.domain.member.repository.MemberRepository;
import com.hk.sampleboard.domain.member.service.MemberService;
import com.hk.sampleboard.domain.member.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegistMemberDTO.Response registMember(RegistMemberDTO.Request request) {
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

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return memberRepository.registMember(request);
    }
    
    //이메일 존재 여부 확인 메서드
    public boolean existEmail (String email) {
        return memberRepository.existEmail(email);
    }

    public boolean existNickname (String nickname) {
        return memberRepository.existNickname(nickname);
    }
}

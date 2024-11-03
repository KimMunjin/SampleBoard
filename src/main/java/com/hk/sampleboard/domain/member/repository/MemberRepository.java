package com.hk.sampleboard.domain.member.repository;

import com.hk.sampleboard.domain.member.dto.RegistMemberDTO;
import com.hk.sampleboard.domain.member.mapper.MemberMapper;
import com.hk.sampleboard.domain.member.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper memberMapper;


    public RegistMemberDTO.Response registMember(RegistMemberDTO.Request request) {
        Member member = RegistMemberDTO.Request.toVo(request);
        memberMapper.registMember(member);
        return RegistMemberDTO.Response.createRegistResponse(member);
    }
    
    //이메일 존재 여부 확인
    public boolean existEmail(String email) {
        Optional<Member> result = memberMapper.findByEmail(email);
        if(result.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean existNickname(String nickname) {
        Optional<Member> result = memberMapper.findByNickname(nickname);
        if(result.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}

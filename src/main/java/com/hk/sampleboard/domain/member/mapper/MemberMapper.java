package com.hk.sampleboard.domain.member.mapper;

import com.hk.sampleboard.domain.member.vo.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {
    
    //회원 가입
    int registMember(Member member);
    
    //email로 회원 검색
    Optional<Member> findByEmail(String email);

    //nickname으로 회원 검색
    Optional<Member> findByNickname(String nickname);



}

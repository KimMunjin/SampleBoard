package com.hk.sampleboard.domain.member.mapper;

import com.hk.sampleboard.domain.member.vo.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    //회원 가입
    int save(Member member);

    //email로 회원 검색
    Optional<Member> findByEmail(String email);

    //nickname으로 회원 검색
    Optional<Member> findByNickname(String nickname);

    //ID로 회원 검색
    Optional<Member> findById(Long memberId);

    //email 존재 여부 확인
    boolean existEmail(String email);

    //nickname 존재 여부 확인
    boolean existNickname(String nickname);

    int update(Member member);


    int delete(Long memberId);
}

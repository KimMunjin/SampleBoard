package com.hk.sampleboard.domain.member.service;

import com.hk.sampleboard.domain.member.dto.RegistMemberDTO;

public interface MemberService {
    RegistMemberDTO.Response registMember(RegistMemberDTO.Request request);
}

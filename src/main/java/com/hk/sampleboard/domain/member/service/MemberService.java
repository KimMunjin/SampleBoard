package com.hk.sampleboard.domain.member.service;

import com.hk.sampleboard.domain.member.dto.*;

public interface MemberService {
    RegistMemberDto.Response registMember(RegistMemberDto.Request request);

    MemberResponse loginMember(LoginMemberDto.Request request);

    String logoutMember(String accessToken, String email);

    UpdateMemberDto.Response updateMember(UpdateMemberDto.Request request);

    DeleteMemberDto.Response deleteMember(DeleteMemberDto.Request request, String accessToken);
}

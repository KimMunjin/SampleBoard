package com.hk.sampleboard.domain.member.service;

import com.hk.sampleboard.domain.member.dto.*;

public interface MemberService {
    RegistMemberDto.Response registMember(RegistMemberDto.Request request);

    MemberResponse loginMember(LoginMemberDto.Request request);

    LogoutMemberDto.Response logoutMember(LogoutMemberDto.Request request);

    UpdateMemberDto.Response updateMember(UpdateMemberDto.Request request);

    DeleteMemberDto.Response deleteMember(DeleteMemberDto.Request request, String accessToken);
}

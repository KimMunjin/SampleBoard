package com.hk.sampleboard.global.security;

import com.hk.sampleboard.domain.member.mapper.MemberMapper;
import com.hk.sampleboard.domain.member.vo.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberMapper.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 이메일 입니다.: " + email));
        return new CustomUserDetails(member);
    }
}

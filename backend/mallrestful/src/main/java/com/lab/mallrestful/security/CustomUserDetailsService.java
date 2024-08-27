package com.lab.mallrestful.security;

import com.lab.mallrestful.domain.Member;
import com.lab.mallrestful.dto.MemberDTO;
import com.lab.mallrestful.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
/*
 * 스프링 시큐리티는 사용자의 인증 처리를 위해서 UserDetailService라는 인터페이스의 구현체를 활용
 * 이를 커스터마이징 하는 클래스
 */
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("-------------------loadUserByUsername-----------------------");

        Member member = memberRepository.getWithRoles(username);
        if (member == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole ->
                        memberRole.name()).collect(Collectors.toList())
        );
        log.info(memberDTO);

        return memberDTO;
    }
}

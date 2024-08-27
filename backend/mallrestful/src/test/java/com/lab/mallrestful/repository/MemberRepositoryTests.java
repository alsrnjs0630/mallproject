package com.lab.mallrestful.repository;

import com.lab.mallrestful.domain.Member;
import com.lab.mallrestful.domain.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMember() {
        for (int i = 0; i < 10; i++) {
            Member member = Member.builder()
                    .email("user" + i + "@jjjj.com")
                    .pw(passwordEncoder.encode("1234"))
                    .nickname("USER" + i)
                    .build();
            member.addRole(MemberRole.USER);
            if (i >= 5) {
                member.addRole(MemberRole.MANAGER);
            }
            if (i >= 8) {
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        }
    }

    @Test
    public void testRead() {
        String email = "user9@jjjj.com";
        Member member = memberRepository.getWithRoles(email);
        log.info("-----------------");
        log.info(member);
    }
}

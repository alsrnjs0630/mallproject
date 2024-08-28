package com.lab.mallrestful.controller;

import com.lab.mallrestful.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
@RequiredArgsConstructor
public class SocialController {
    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public String[] getMemberFromKakao(String accessToken) {

        log.info("access Token ");
        log.info(accessToken);

        // accessToken으로 사용자 이메일 추출
        memberService.getKakaoMember(accessToken);

        return new String[]{"jeus", "hong", "kim"};
    }
}

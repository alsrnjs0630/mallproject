package com.lab.mallrestful.service;

import com.lab.mallrestful.dto.MemberDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberService {
    MemberDTO getKakaoMember(String accessToken);
}

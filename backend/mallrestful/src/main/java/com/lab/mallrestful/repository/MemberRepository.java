package com.lab.mallrestful.repository;

import com.lab.mallrestful.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {
    // 조회 시에 권한 목록까지 같이 로딩함
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m where m.email =:email")
    Member getWithRoles(@Param("email") String email);
}

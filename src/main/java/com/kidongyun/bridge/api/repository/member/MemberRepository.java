package com.kidongyun.bridge.api.repository.member;

import com.kidongyun.bridge.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, String>, MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);
}

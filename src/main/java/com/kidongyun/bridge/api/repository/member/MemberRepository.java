package com.kidongyun.bridge.api.repository.member;

import com.kidongyun.bridge.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, String>, MemberRepositoryCustom {
}

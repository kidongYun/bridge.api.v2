package com.kidongyun.bridge.api.repository.priority;

import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface PriorityRepository extends JpaRepository<Priority, Long>, PriorityRepositoryCustom {
    Optional<Priority> findByIdAndMember(Long id, Member member);

    Optional<Priority> findByIdAndMemberEmail(Long id, String email);
}

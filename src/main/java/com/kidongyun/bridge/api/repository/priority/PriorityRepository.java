package com.kidongyun.bridge.api.repository.priority;

import com.kidongyun.bridge.api.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface PriorityRepository extends JpaRepository<Priority, Long>, PriorityRepositoryCustom {
}

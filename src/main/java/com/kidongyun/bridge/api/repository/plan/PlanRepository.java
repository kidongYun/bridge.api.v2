package com.kidongyun.bridge.api.repository.plan;

import com.kidongyun.bridge.api.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface PlanRepository extends JpaRepository<Plan, Long>, PlanRepositoryCustom {
}

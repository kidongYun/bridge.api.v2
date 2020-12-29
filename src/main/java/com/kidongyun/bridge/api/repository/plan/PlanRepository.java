package com.kidongyun.bridge.api.repository.plan;

import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface PlanRepository extends CellRepository<Plan>, PlanRepositoryCustom {
}

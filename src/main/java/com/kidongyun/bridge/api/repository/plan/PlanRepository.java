package com.kidongyun.bridge.api.repository.plan;

import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.cell.CellRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public interface PlanRepository extends CellRepository<Plan>, PlanRepositoryCustom {
    Set<Plan> findByObjective(Objective objective);

    Set<Plan> findByObjectiveId(Long id);
}

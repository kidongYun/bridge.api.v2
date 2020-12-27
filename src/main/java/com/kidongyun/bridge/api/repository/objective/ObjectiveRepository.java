package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ObjectiveRepository extends CellRepository<Objective>, ObjectiveRepositoryCustom {
    List<Objective> findByParent(Long parent);
}

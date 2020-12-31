package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Transactional
public interface ObjectiveRepository extends CellRepository<Objective>, ObjectiveRepositoryCustom {
    Set<Objective> findByParent(Objective parent);

    Set<Objective> findByParentAndMember(Objective parent, Member member);
}
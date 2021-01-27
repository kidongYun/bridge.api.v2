package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.cell.CellRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public interface ObjectiveRepository extends CellRepository<Objective>, ObjectiveRepositoryCustom {
    Set<Objective> findByParent(Objective parent);

    Set<Objective> findByParentAndMember(Objective parent, Member member);

    Set<Objective> findByPriority(Priority priority);

    Set<Objective> findByPriorityId(Long id);

    Set<Objective> findByMemberEmailAndPriorityIdAndParentIdAndStatus(String email, Long priorityId, Long parentId, Cell.Status status);

    Set<Objective> findByMemberEmailAndPriorityIdAndParentId(String email, Long priorityId, Long parentId);

    Set<Objective> findByMemberEmailAndPriorityIdAndStatus(String email, Long priorityId, Cell.Status status);

    Set<Objective> findByMemberEmailAndPriorityId(String email, Long priorityId);

    Set<Objective> findByMemberEmailAndParentIdAndStatus(String email, Long priorityId, Cell.Status status);

    Set<Objective> findByMemberEmailAndParentId(String email, Long parentId);

    Set<Objective> findByPriorityIdAndParentIdAndStatus(Long priorityId, Long parentId, Cell.Status status);

    Set<Objective> findByPriorityIdAndParentId(Long priorityId, Long parentId);

    Set<Objective> findByPriorityIdAndStatus(Long priorityId, Cell.Status status);

    Set<Objective> findByParentIdAndStatus(Long parentId, Cell.Status status);

    Set<Objective> findByParentId(Long parentId);
}
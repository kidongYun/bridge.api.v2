package com.kidongyun.bridge.api.repository.cell;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Transactional
public interface CellRepository<T extends Cell> extends JpaRepository<T, Long>, CellRepositoryCustom<T> {
    Set<T> findByType(Cell.Type type);

    Set<T> findByTypeOrderByStartDate(Cell.Type type);

    Set<T> findByTypeOrderByEndDate(Cell.Type type);

    Set<T> findByMember(Member member);

    Set<T> findByMemberEmail(String email);

    Set<T> findByMemberEmailOrderByStartDate(String email);

    Set<T> findByMemberEmailOrderByEndDate(String email);

    Optional<T> findByIdAndType(Long id, Cell.Type type);
}

package com.kidongyun.bridge.api.repository.cell;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
public interface CellRepository<T extends Cell> extends JpaRepository<T, Long>, CellRepositoryCustom<T> {
    Set<T> findByType(Cell.Type type);

    Set<T> findByMember(Member member);
}

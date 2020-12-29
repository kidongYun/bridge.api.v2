package com.kidongyun.bridge.api.repository.cell;

import com.kidongyun.bridge.api.entity.Cell;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface CellRepository<T extends Cell> extends JpaRepository<T, Long>, CellRepositoryCustom<T> {
    List<T> findByType(Cell.Type type);
}

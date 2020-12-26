package com.kidongyun.bridge.api.repository.cell;

import com.kidongyun.bridge.api.entity.Cell;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CellRepositoryCustomImpl<T extends Cell> implements CellRepositoryCustom<T> {
    private final JPAQueryFactory queryFactory;
}

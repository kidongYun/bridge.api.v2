package com.kidongyun.bridge.api.repository.priority;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PriorityRepositoryCustomImpl implements PriorityRepositoryCustom {
    private final JPAQueryFactory queryFactory;
}

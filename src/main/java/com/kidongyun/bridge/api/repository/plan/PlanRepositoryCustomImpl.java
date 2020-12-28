package com.kidongyun.bridge.api.repository.plan;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlanRepositoryCustomImpl implements PlanRepositoryCustom {
    private final JPAQueryFactory queryFactory;
}

package com.kidongyun.bridge.api.repository.objective;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectiveRepositoryCustomImpl implements ObjectiveRepositoryCustom {
   private final JPAQueryFactory queryFactory;
}

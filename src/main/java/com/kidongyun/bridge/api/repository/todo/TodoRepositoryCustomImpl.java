package com.kidongyun.bridge.api.repository.todo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {
    private final JPAQueryFactory queryFactory;
}

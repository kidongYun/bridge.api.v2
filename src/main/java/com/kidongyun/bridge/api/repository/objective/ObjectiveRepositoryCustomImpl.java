package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kidongyun.bridge.api.entity.QObjective.objective;

@RequiredArgsConstructor
public class ObjectiveRepositoryCustomImpl implements ObjectiveRepositoryCustom {
   private final JPAQueryFactory queryFactory;

   @Override
   public Optional<Objective> findById(Long id) {
      return Optional.ofNullable(queryFactory.selectFrom(objective)
              .where(objective.id.eq(id))
              .where(objective.type.eq(Cell.Type.Objective))
              .fetchOne());
   }
}

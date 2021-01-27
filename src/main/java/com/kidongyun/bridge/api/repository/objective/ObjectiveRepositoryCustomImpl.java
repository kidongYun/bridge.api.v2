package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Objective;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.kidongyun.bridge.api.entity.QObjective.objective;

@RequiredArgsConstructor
public class ObjectiveRepositoryCustomImpl implements ObjectiveRepositoryCustom {
   private final JPAQueryFactory queryFactory;

   @Override
   public List<Objective> findByObjective(Objective obj) {
      return queryFactory.selectFrom(objective)
              .where(objective.status.eq(obj.getStatus().name()))
              .fetch();
   }
}

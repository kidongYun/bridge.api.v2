package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Objective;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static com.kidongyun.bridge.api.entity.QObjective.objective;

@Slf4j
@RequiredArgsConstructor
public class ObjectiveRepositoryCustomImpl implements ObjectiveRepositoryCustom {
   private final JPAQueryFactory queryFactory;

   @Override
   public List<Objective> findByObjective(Objective obj) {
      JPAQuery<Objective> query = queryFactory.selectFrom(objective);

      if(Objects.nonNull(obj.getId())) {
         return query.where(objective.id.eq(obj.getId())).fetch();
      }

      if(Objects.nonNull(obj.getStartDate())) {
         query.where(objective.startDate.eq(obj.getStartDate()));
      }

      if(Objects.nonNull(obj.getEndDate())) {
         query.where(objective.endDate.eq(obj.getEndDate()));
      }

      if(Objects.nonNull(obj.getTitle())) {
         query.where(objective.title.contains(obj.getTitle()));
      }

      return query.fetch();
   }
}

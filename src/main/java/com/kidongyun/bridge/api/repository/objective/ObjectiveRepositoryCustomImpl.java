package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

      if(Objects.nonNull(obj.getStartDateTime())) {
         LocalDateTime from = obj.getStartDateTime().toLocalDate().atStartOfDay();
         LocalDateTime to = LocalDateTime.of(obj.getStartDateTime().toLocalDate(), LocalTime.of(23, 59, 59));

         query.where(objective.startDateTime.between(from, to));
      }

      if(Objects.nonNull(obj.getEndDateTime())) {
         query.where(objective.endDateTime.eq(obj.getEndDateTime()));
      }

      if(Objects.nonNull(obj.getTitle())) {
         query.where(objective.title.contains(obj.getTitle()));
      }

      return query.fetch();
   }
}

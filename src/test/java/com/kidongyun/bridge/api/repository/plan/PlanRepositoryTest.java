package com.kidongyun.bridge.api.repository.plan;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class PlanRepositoryTest {
    @Autowired
    ObjectiveRepository objectiveRepository;
    @Autowired
    PlanRepository planRepository;

    @Test
    public void findByObjective_normal() {
        /* Arrange */
        Objective objective1 = Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build();
        objectiveRepository.save(objective1);

        Objective objective2 = Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title2").description("desc2").build();
        objectiveRepository.save(objective2);

        planRepository.save(Plan.builder().id(3L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content3").objective(objective1).build());

        planRepository.save(Plan.builder().id(4L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content4").objective(objective1).build());

        planRepository.save(Plan.builder().id(5L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content5").objective(objective2).build());

        planRepository.save(Plan.builder().id(6L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content6").objective(objective2).build());

        /* Act */
        Set<Plan> results = planRepository.findByObjective(Objective.builder().id(1L).build());

        /* Assert */
        results.forEach(plan -> {
            log.info("YKD : " + plan.getId());
            log.info("YKD : " + plan.getContent());
            log.info("YKD : " + plan.getObjective().getId());
        });
    }

    @Test
    public void findByObjectiveId_normal() {
        /* Arrange */
        Objective objective1 = Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build();
        objectiveRepository.save(objective1);

        Objective objective2 = Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title2").description("desc2").build();
        objectiveRepository.save(objective2);

        planRepository.save(Plan.builder().id(3L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content3").objective(objective1).build());

        planRepository.save(Plan.builder().id(4L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content4").objective(objective1).build());

        planRepository.save(Plan.builder().id(5L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content5").objective(objective2).build());

        planRepository.save(Plan.builder().id(6L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").content("content6").objective(objective2).build());

        /* Act */
        Set<Plan> results = planRepository.findByObjectiveId(1L);

        results.forEach(plan -> {
            log.info("YKD : " + plan.getId());
            log.info("YKD : " + plan.getContent());
            log.info("YKD : " + plan.getObjective().getId());
        });
    }
}
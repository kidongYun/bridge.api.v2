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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

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
        Objective objective1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("title1").description("desc1").build();
        objectiveRepository.save(objective1);

        Objective objective2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("title2").description("desc2").build();
        objectiveRepository.save(objective2);

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content3").objective(objective1).build());

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content4").objective(objective1).build());

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content5").objective(objective2).build());

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content6").objective(objective2).build());

        /* Act */
        Set<Plan> results = planRepository.findByObjective(objective1);

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        results.forEach(plan -> {
            assertThat(plan.getObjective().getId()).isEqualTo(objective1.getId());
        });
    }

    @Test
    public void findByObjectiveId_normal() {
        /* Arrange */
        Objective objective1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("title1").description("desc1").build();
        objectiveRepository.save(objective1);

        Objective objective2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("title2").description("desc2").build();
        objectiveRepository.save(objective2);

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content3").objective(objective1).build());

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content4").objective(objective1).build());

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content5").objective(objective2).build());

        planRepository.save(Plan.builder().type(Cell.Type.Plan).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).content("content6").objective(objective2).build());

        /* Act */
        Set<Plan> results = planRepository.findByObjectiveId(objective2.getId());

        assertThat(results.size()).isEqualTo(2);
        results.forEach(plan -> {
            assertThat(plan.getObjective().getId()).isEqualTo(objective2.getId());
        });
    }
}
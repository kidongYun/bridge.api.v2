package com.kidongyun.bridge.api.repository;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class CellRepositoryTest {

    @Autowired
    CellRepository<Objective> objectiveRepository;

    @Autowired
    CellRepository<Plan> planRepository;

    /** when you add each objective and plan values, findByType(Cell.Type.Objective) function() should be returned the collection had only 1 sized value  */
    @Test
    public void findByType_ObjectiveShouldBeNormal() {
        Objective objective = Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now()).status("completed").type(Cell.Type.Objective)
                .title("I would like to become senoir developer").description("I always study the techniques of coding for 3 hours").priority(1).build();
        objectiveRepository.save(objective);

        Plan plan = Plan.builder().id(3L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("Let's study Spring Data JPA").build();
        planRepository.save(plan);

        List<Objective> objectives = objectiveRepository.findByType(Cell.Type.Objective);

        log.info("YKD : " + objectives.toString());
        assertThat(objectives.size()).isEqualTo(1);
    }
}
package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Objective;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class ObjectiveRepositoryTest {

    @Autowired
    ObjectiveRepository objectiveRepository;

    @Test
    public void findByParent() {
        Objective parent_objective = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build();

        Objective child_objective = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(1L).build();

        Objective child_objective2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(1L).build();

        Objective child_objective3 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(2L).build();

        objectiveRepository.save(parent_objective);
        objectiveRepository.save(child_objective);
        objectiveRepository.save(child_objective2);
        objectiveRepository.save(child_objective3);

        List<Objective> objectives = objectiveRepository.findByParent(1L);
        assertThat(objectives.size())
        log.info("YKD : " + objectives.toString());
    }
}
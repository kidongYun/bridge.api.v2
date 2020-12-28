package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class ObjectiveRepositoryTest {

    @Autowired
    ObjectiveRepository objectiveRepository;


    @Test
    public void findByParent_normal() {
        /* Arrange */
        Set<Objective> stubs = new HashSet<>();

        stubs.add(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build());

        stubs.add(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(1L).build());

        stubs.add(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(1L).build());

        stubs.add(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(2L).build());

        for(Objective stub : stubs) {
            objectiveRepository.save(stub);
        }

        /* Act */
        Set<Objective> result = objectiveRepository.findByParent(1L);

        /* Assert */
        assertThat(result.size()).isEqualTo(2);
        for(Objective objective : result) {
            assertThat(objective.getParent()).isEqualTo(1);
        }
    }

    @Test
    public void findByParentAndMember_normal() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(1L).member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(1L).member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(2L).member(john).build());

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title5").description("desc5").member(julia).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title6").description("desc6").parent(1L).member(julia).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title7").description("desc7").parent(1L).member(julia).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title8").description("desc8").parent(2L).member(julia).build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByParentAndMember(1L, Member.builder().email("john@gmail.com").build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent()).isEqualTo(1L);
            assertThat(result.getMember()).isEqualTo(john);
        }
    }

    @Test
    public void findById_normal() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(1L).member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(1L).member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(2L).member(john).build());

        Objective objective = objectiveRepository.findById(2L)
                .orElseThrow(() -> new Exception("We can't find the objective you told me"));

        assertThat(objective).isNotNull();
        assertThat(objective.getId()).isEqualTo(2L);
    }
}
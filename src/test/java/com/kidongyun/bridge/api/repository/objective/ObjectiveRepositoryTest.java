package com.kidongyun.bridge.api.repository.objective;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class ObjectiveRepositoryTest {

    @Autowired
    ObjectiveRepository objectiveRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void findByType_normal() {
        /* Arrange */
        objectiveRepository.save(Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build());

        objectiveRepository.save(Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().id(3L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().id(4L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(Objective.builder().id(2L).build()).build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByType(Cell.Type.Objective);

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        for(Objective result : results) {
            assertThat(result.getType()).isEqualTo(Cell.Type.Objective);
        }
    }

    @Test
    public void findByParent_normal() {
        /* Arrange */
        objectiveRepository.save(Objective.builder().id(1L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build());

        objectiveRepository.save(Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().id(3L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().id(4L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(Objective.builder().id(2L).build()).build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByParent(Objective.builder().id(1L).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getId()).isEqualTo(1L);
        }
    }

    @Test
    public void findByParentAndMember_normal() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        memberRepository.save(john);

        objectiveRepository.save(Objective.builder().id(1L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").member(john).build());

        objectiveRepository.save(Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).member(john).build());

        objectiveRepository.save(Objective.builder().id(3L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).member(john).build());

        objectiveRepository.save(Objective.builder().id(4L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(Objective.builder().id(2L).build()).member(john).build());

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();
        memberRepository.save(julia);

        objectiveRepository.save(Objective.builder().id(5L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title5").description("desc5").parent(Objective.builder().id(3L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().id(6L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title6").description("desc6").parent(Objective.builder().id(3L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().id(7L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title7").description("desc7").parent(Objective.builder().id(4L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().id(8L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title8").description("desc8").parent(Objective.builder().id(4L).build()).member(julia).build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByParentAndMember(Objective.builder().id(1L).build(), john);

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getId()).isEqualTo(1L);
            assertThat(result.getMember().getEmail()).isEqualTo("john@gmail.com");
        }
    }

    @Test
    public void findById_normal() throws Exception {
        /* Arrange */
        objectiveRepository.save(Objective.builder().id(1L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build());

        objectiveRepository.save(Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().id(3L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().id(4L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(Objective.builder().id(2L).build()).build());

        /* Act */
        Objective objective = objectiveRepository.findById(2L)
                .orElseThrow(() -> new Exception("We can't find the objective you told me"));

        /* Assert */
        assertThat(objective).isNotNull();
        assertThat(objective.getId()).isEqualTo(2L);
    }
}
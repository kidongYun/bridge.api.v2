package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
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

    @Autowired
    PriorityRepository priorityRepository;

    @Test
    public void findByType_normal() {
        /* Arrange */
        objectiveRepository.save(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build());

        objectiveRepository.save(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
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
        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
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

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).member(john).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(Objective.builder().id(2L).build()).member(john).build());

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();
        memberRepository.save(julia);

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title5").description("desc5").parent(Objective.builder().id(3L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title6").description("desc6").parent(Objective.builder().id(3L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title7").description("desc7").parent(Objective.builder().id(4L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title8").description("desc8").parent(Objective.builder().id(4L).build()).member(julia).build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByParentAndMember(Objective.builder().id(1L).build(), Member.builder().email("john@gmail.com").build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getId()).isEqualTo(1L);
            assertThat(result.getMember().getEmail()).isEqualTo("john@gmail.com");
        }
    }

    @Test
    public void findByPriority_normal() {
        /* Arrange */
        Priority priority1 = Priority.builder().level(1).description("Important").build();
        priorityRepository.save(priority1);

        Priority priority2 = Priority.builder().level(2).description("Normal").build();
        priorityRepository.save(priority2);

        objectiveRepository.save(Objective.builder().id(1L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").priority(priority1).build());

        objectiveRepository.save(Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").priority(priority1).build());

        objectiveRepository.save(Objective.builder().id(3L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").priority(priority2).build());

        objectiveRepository.save(Objective.builder().id(4L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").priority(priority2).build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByPriority(Priority.builder().level(1).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getPriority().getLevel()).isEqualTo(1);
            assertThat(result.getPriority().getDescription()).isEqualTo("Important");
        }
    }

    @Test
    public void findByPriorityLevel_normal() {
        /* Arrange */
        Priority priority1 = Priority.builder().level(1).description("Important").build();
        priorityRepository.save(priority1);

        Priority priority2 = Priority.builder().level(2).description("Normal").build();
        priorityRepository.save(priority2);

        objectiveRepository.save(Objective.builder().id(1L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").priority(priority1).build());

        objectiveRepository.save(Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").priority(priority1).build());

        objectiveRepository.save(Objective.builder().id(3L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").priority(priority2).build());

        objectiveRepository.save(Objective.builder().id(4L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").priority(priority2).build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByPriorityLevel(1);

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getPriority().getLevel()).isEqualTo(1);
            assertThat(result.getPriority().getDescription()).isEqualTo("Important");
        }
    }
}
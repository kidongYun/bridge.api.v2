package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
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

    List<Member> memberStubs;
    List<Priority> priorityStubs;
    List<Objective> objectiveStubs;

    @Before
    public void setUp() {
        /* Arrange */
        memberStubs = List.of(
                Member.builder().email("john@gmail.com").password("q1w2e3r4").build(),
                Member.builder().email("julia@gmail.com").password("julia123").build()
        );

        priorityStubs = List.of(
                Priority.builder().level(1).description("Important").member(memberStubs.get(0)).build(),
                Priority.builder().level(2).description("Normal").member(memberStubs.get(0)).build(),
                Priority.builder().level(3).description("UnImportant").member(memberStubs.get(0)).build(),
                Priority.builder().level(1).description("Important").member(memberStubs.get(1)).build(),
                Priority.builder().level(2).description("Normal").member(memberStubs.get(1)).build(),
                Priority.builder().level(3).description("UnImportant").member(memberStubs.get(1)).build()
        );

        priorityStubs.forEach(priority -> priorityRepository.save(priority));

        Objective objectiveJohn1 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").member(john).priority(priorityJohn1).build();
        objectiveRepository.save(objectiveJohn1);

        Objective objectiveJohn2 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(objectiveJohn1).member(john).priority(priorityJohn1).build();
        objectiveRepository.save(objectiveJohn2);

        Objective objectiveJohn3 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(objectiveJohn1).member(john).priority(priorityJohn2).build();
        objectiveRepository.save(objectiveJohn3);

        Objective objectiveJohn4 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(objectiveJohn2).member(john).priority(priorityJohn3).build();
        objectiveRepository.save(objectiveJohn4);

        Objective objectiveJulia1 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title5").description("desc5").member(julia).priority(priorityJulia1).build();
        objectiveRepository.save(objectiveJulia1);

        Objective objectiveJulia2 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title6").description("desc6").parent(objectiveJulia1).member(julia).priority(priorityJulia2).build();
        objectiveRepository.save(objectiveJulia2);

        Objective objectiveJulia3 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title7").description("desc7").parent(objectiveJulia2).member(julia).priority(priorityJulia2).build();
        objectiveRepository.save(objectiveJulia3);

        Objective objectiveJulia4 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title8").description("desc8").parent(objectiveJulia3).member(julia).priority(priorityJulia2).build();
        objectiveRepository.save(objectiveJulia4);

        log.info("YKD : " + priorityJulia2.toString());
    }

    @Test
    public void save_normal() {
        /* Arrange, Act */
        Objective obj = objectiveRepository.save(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build());

        assertThat(obj).isNotNull();
        assertThat(obj.getTitle()).isEqualTo("title1");
        assertThat(obj.getDescription()).isEqualTo("desc1");
    }

    @Test
    public void findByType_normal() {
        /* Act */
        Set<Objective> results = objectiveRepository.findByType(Cell.Type.Objective);

        /* Assert */
        assertThat(results.size()).isEqualTo(8);
        for(Objective result : results) {
            assertThat(result.getType()).isEqualTo(Cell.Type.Objective);
        }
    }

    @Test
    public void findByParent_normal() {
        /* Act */
        Set<Objective> results = objectiveRepository.findByParent(Objective.builder().id(7L).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getTitle()).isEqualTo("title1");
            assertThat(result.getParent().getDescription()).isEqualTo("desc1");
        }
    }

    @Test
    public void findByParentAndMember_normal() {
        /* Act */
        Set<Objective> results = objectiveRepository.findByParentAndMember(Objective.builder().id(7L).build(), Member.builder().email("john@gmail.com").build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getTitle()).isEqualTo("title1");
            assertThat(result.getParent().getDescription()).isEqualTo("desc1");
            assertThat(result.getMember().getEmail()).isEqualTo("john@gmail.com");
        }
    }

    @Test
    public void findByPriority_normal() {
        /* Act */
        Set<Objective> results = objectiveRepository.findByPriority(Priority.builder().id(5L).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(3);
        for(Objective result : results) {
            assertThat(result.getPriority().getLevel()).isEqualTo(2);
            assertThat(result.getPriority().getDescription()).isEqualTo("Normal");
        }
    }

    @Test
    public void findByPriorityId_normal() {
        /* Act */
        Set<Objective> results = objectiveRepository.findByPriorityId(5L);

        /* Assert */
        assertThat(results.size()).isEqualTo(3);
        for(Objective result : results) {
            assertThat(result.getPriority().getLevel()).isEqualTo(2);
            assertThat(result.getPriority().getDescription()).isEqualTo("Normal");
        }
    }
}
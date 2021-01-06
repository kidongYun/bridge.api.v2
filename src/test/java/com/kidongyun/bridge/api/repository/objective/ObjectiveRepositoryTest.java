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
import java.util.ArrayList;
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

    List<Member> memberStubs = new ArrayList<>();
    List<Priority> priorityStubs = new ArrayList<>();
    List<Objective> objectiveStubs = new ArrayList<>();

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
                Priority.builder().level(3).description("Unimportant").member(memberStubs.get(1)).build()
        );

        priorityStubs.forEach(priority -> priorityRepository.save(priority));

        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").member(memberStubs.get(0)).priority(priorityStubs.get(0)).build());
        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(objectiveStubs.get(0)).member(memberStubs.get(0)).priority(priorityStubs.get(0)).build());
        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(objectiveStubs.get(0)).member(memberStubs.get(0)).priority(priorityStubs.get(1)).build());
        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(objectiveStubs.get(1)).member(memberStubs.get(0)).priority(priorityStubs.get(2)).build());
        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title5").description("desc5").member(memberStubs.get(1)).priority(priorityStubs.get(3)).build());
        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title6").description("desc6").parent(objectiveStubs.get(4)).member(memberStubs.get(1)).priority(priorityStubs.get(4)).build());
        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title7").description("desc7").parent(objectiveStubs.get(5)).member(memberStubs.get(1)).priority(priorityStubs.get(5)).build());
        objectiveStubs.add(Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title8").description("desc8").parent(objectiveStubs.get(6)).member(memberStubs.get(1)).priority(priorityStubs.get(5)).build());

        objectiveStubs.forEach(obj -> objectiveRepository.save(obj));
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
        Set<Objective> results = objectiveRepository.findByParent(objectiveStubs.get(0));

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
        Set<Objective> results = objectiveRepository.findByParentAndMember(objectiveStubs.get(0), memberStubs.get(0));

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
        Set<Objective> results = objectiveRepository.findByPriority(priorityStubs.get(5));

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getPriority().getLevel()).isEqualTo(3);
            assertThat(result.getPriority().getDescription()).isEqualTo("Unimportant");
        }
    }

    @Test
    public void findByPriorityId_normal() {
        /* Act */
        Set<Objective> results = objectiveRepository.findByPriorityId(priorityStubs.get(1).getId());

        /* Assert */
        assertThat(results.size()).isEqualTo(1);
        for(Objective result : results) {
            assertThat(result.getPriority().getLevel()).isEqualTo(2);
            assertThat(result.getPriority().getDescription()).isEqualTo("Normal");
        }
    }
}
package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.*;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.extern.slf4j.Slf4j;;
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
    public void save_normalCase() {
        /* Arrange, Act */
        Objective obj = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("title1").description("desc1").build();
        Objective result = objectiveRepository.save(obj);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(obj.getId());
        assertThat(result.getTitle()).isEqualTo(obj.getTitle());
        assertThat(result.getDescription()).isEqualTo(obj.getDescription());
    }

    @Test
    public void findByParent_normalCase() {
        /* Arrange */
        Objective obj1 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("title1").description("desc1").build();
        objectiveRepository.save(obj1);

        Objective obj2 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("title2").description("desc2").parent(obj1).build();
        objectiveRepository.save(obj2);

        Objective obj3 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("title3").description("desc3").parent(obj1).build();
        objectiveRepository.save(obj3);

        /* Act */
        Set<Objective> results = objectiveRepository.findByParent(obj1);

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getId()).isEqualTo(obj1.getId());
            assertThat(result.getParent().getTitle()).isEqualTo(obj1.getTitle());
            assertThat(result.getParent().getDescription()).isEqualTo(obj1.getDescription());
        }
    }

    @Test
    public void findByParentAndMember_normal() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").parent(objOfJohn1).member(john).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").parent(objOfJohn1).member(john).build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia1 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("titleOfJulia1").description("descOfJulia1").member(julia).build();
        objectiveRepository.save(objOfJulia1);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("titleOfJulia2").description("descOfJulia2").parent(objOfJulia1).member(julia).build();
        objectiveRepository.save(objOfJulia2);

        Objective objOfJulia3 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("titleOfJulia3").description("descOfJulia3").parent(objOfJulia1).member(julia).build();
        objectiveRepository.save(objOfJulia3);

        /* Act */
        Set<Objective> results = objectiveRepository.findByParentAndMember(objOfJohn1, john);

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getId()).isEqualTo(objOfJohn1.getId());
            assertThat(result.getParent().getTitle()).isEqualTo(objOfJohn1.getTitle());
            assertThat(result.getParent().getDescription()).isEqualTo(objOfJohn1.getDescription());
            assertThat(result.getMember().getEmail()).isEqualTo(john.getEmail());
        }
    }

    @Test
    public void findByPriority_normal() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priorityOfJohn1 = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priorityOfJohn1);

        Priority priorityOfJohn2 = Priority.builder().level(2).description("Normal").member(john).build();
        priorityRepository.save(priorityOfJohn2);

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("titleOfJohn3").description("descOfJohn3").member(john).priority(priorityOfJohn2).build();
        objectiveRepository.save(objOfJohn3);

        /* Act */
        Set<Objective> results = objectiveRepository.findByPriority(priorityOfJohn1);

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getPriority().getId()).isEqualTo(priorityOfJohn1.getId());
            assertThat(result.getPriority().getLevel()).isEqualTo(priorityOfJohn1.getLevel());
            assertThat(result.getPriority().getDescription()).isEqualTo(priorityOfJohn1.getDescription());
        }
    }

    @Test
    public void findByPriorityId_normal() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priorityOfJohn1 = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priorityOfJohn1);

        Priority priorityOfJohn2 = Priority.builder().level(2).description("Normal").member(john).build();
        priorityRepository.save(priorityOfJohn2);

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").member(john).priority(priorityOfJohn2).build();
        objectiveRepository.save(objOfJohn3);

        /* Act */
        Set<Objective> results = objectiveRepository.findByPriorityId(priorityOfJohn1.getId());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getPriority().getId()).isEqualTo(priorityOfJohn1.getId());
            assertThat(result.getPriority().getLevel()).isEqualTo(priorityOfJohn1.getLevel());
            assertThat(result.getPriority().getDescription()).isEqualTo(priorityOfJohn1.getDescription());
        }
    }
}
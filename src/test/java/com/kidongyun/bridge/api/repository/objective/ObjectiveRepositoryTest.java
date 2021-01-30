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

import java.time.LocalDate;
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

    @Test
    public void save_normalCase() {
        /* Arrange, Act */
        Objective obj = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("title1").description("desc1").build();
        Objective result = objectiveRepository.save(obj);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(obj.getId());
        assertThat(result.getTitle()).isEqualTo(obj.getTitle());
        assertThat(result.getDescription()).isEqualTo(obj.getDescription());
    }

    @Test
    public void findByParent_normalCase() {
        /* Arrange */
        Objective obj1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("title1").description("desc1").build();
        objectiveRepository.save(obj1);

        Objective obj2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("title2").description("desc2").parent(obj1).build();
        objectiveRepository.save(obj2);

        Objective obj3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("title3").description("desc3").parent(obj1).build();
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

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").parent(objOfJohn1).member(john).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").parent(objOfJohn1).member(john).build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJulia1").description("descOfJulia1").member(julia).build();
        objectiveRepository.save(objOfJulia1);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJulia2").description("descOfJulia2").parent(objOfJulia1).member(julia).build();
        objectiveRepository.save(objOfJulia2);

        Objective objOfJulia3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJulia3").description("descOfJulia3").parent(objOfJulia1).member(julia).build();
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

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn3").description("descOfJohn3").member(john).priority(priorityOfJohn2).build();
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

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").member(john).priority(priorityOfJohn2).build();
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

    @Test
    public void findByObjective_whenIdIsOffered_thenReturnOneHasSameId() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").parent(objOfJohn1).member(john).build();
        objectiveRepository.save(objOfJohn2);

        /* Act */
        List<Objective> results = objectiveRepository.findByObjective(Objective.builder().id(objOfJohn1.getId()).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getId()).isEqualTo(objOfJohn1.getId());
        assertThat(results.get(0).getTitle()).isEqualTo(objOfJohn1.getTitle());
        assertThat(results.get(0).getDescription()).isEqualTo(objOfJohn1.getDescription());
        assertThat(results.get(0).getStartDate()).isEqualTo(objOfJohn1.getStartDate());
        assertThat(results.get(0).getEndDate()).isEqualTo(objOfJohn1.getEndDate());
    }

    @Test
    public void findByObjective_whenStartDateIsOffered_thenReturnObjectivesHaveSameStartDate() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").member(john).build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJulia2").description("descOfJulia2").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        /* Act */
        List<Objective> results = objectiveRepository.findByObjective(Objective.builder().startDate(LocalDate.now()).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getStartDate()).isBefore(LocalDate.now().plusDays(1));
            assertThat(result.getStartDate()).isAfter(LocalDate.now().minusDays(1));
            assertThat(result.getStartDate()).isEqualTo(LocalDate.now());
        }
    }

    @Test
    public void findByObjective_whenEndDateIsOffered_thenReturnObjectivesHaveSameEndDate() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now().minusDays(3)).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1)).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").member(john).build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJulia2").description("descOfJulia2").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        /* Act */
        List<Objective> results = objectiveRepository.findByObjective(Objective.builder().endDate(LocalDate.now()).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getEndDate()).isBefore(LocalDate.now().plusDays(1));
            assertThat(result.getEndDate()).isAfter(LocalDate.now().minusDays(1));
            assertThat(result.getEndDate()).isEqualTo(LocalDate.now());
        }
    }

    @Test
    public void findByObjective_whenTitleIsOffered_thenReturnObjectivesContainSameTitle() {
        /* Arrange */
        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).title("titleOfJohn1").build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).title("titleOfJohn2").build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).title("titleOfJohn3").build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).title("titleOfJulia2").build();
        objectiveRepository.save(objOfJulia2);

        /* Act */
        String title = "John";
        List<Objective> results = objectiveRepository.findByObjective(Objective.builder().title(title).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(3);
        for(Objective result : results) {
            assertThat(result.getTitle().contains(title)).isTrue();
        }
    }

    @Test
    public void findByObjective_whenDescriptionIsOffered_thenReturnObjectivesContainSameDescription() {
        /* Arrange */
        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).description("descOfJohn1").build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).description("descOfJohn2").build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).description("descOfJohn3").build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).description("descOfJulia2").build();
        objectiveRepository.save(objOfJulia2);

        /* Act */
        String description = "John";
        List<Objective> results = objectiveRepository.findByObjective(Objective.builder().description(description).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(3);
        for(Objective result : results) {
            assertThat(result.getDescription().contains(description)).isTrue();
        }
    }

    @Test
    public void findByObjective_whenEmailIsOffered_thenReturnObjectivesHaveSameEmail() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now().minusDays(3)).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1)).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").member(john).build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJulia2").description("descOfJulia2").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        /* Act */
        List<Objective> resultsOfJohn = objectiveRepository.findByObjective(Objective.builder().member(john).build());
        List<Objective> resultsOfJulia = objectiveRepository.findByObjective(Objective.builder().member(julia).build());

        /* Assert */
        assertThat(resultsOfJohn.size()).isEqualTo(3);
        for(Objective result : resultsOfJohn) {
            assertThat(result.getMember().getEmail()).isEqualTo(john.getEmail());
        }

        assertThat(resultsOfJulia.size()).isEqualTo(1);
        for(Objective result : resultsOfJulia) {
            assertThat(result.getMember().getEmail()).isEqualTo(julia.getEmail());
        }
    }

    @Test
    public void findByObjective_whenPriorityIsOffered_thenReturnObjectivesHaveSamePriority() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priorityOfJohn1 = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priorityOfJohn1);

        Priority priorityOfJohn2 = Priority.builder().level(2).description("Normal").member(john).build();
        priorityRepository.save(priorityOfJohn2);

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1")
                .description("descOfJohn1").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2")
                .description("descOfJohn2").member(john).priority(priorityOfJohn1).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn3")
                .description("descOfJohn3").member(john).priority(priorityOfJohn2).build();
        objectiveRepository.save(objOfJohn3);

        /* Act */
        List<Objective> importantObjs = objectiveRepository.findByObjective(Objective.builder().priority(priorityOfJohn1).build());
        List<Objective> normalObjs = objectiveRepository.findByObjective(Objective.builder().priority(priorityOfJohn2).build());

        /* Assert */
        assertThat(importantObjs.size()).isEqualTo(2);
        for(Objective importantObj : importantObjs) {
            assertThat(importantObj.getPriority().getId()).isEqualTo(priorityOfJohn1.getId());
        }

        assertThat(normalObjs.size()).isEqualTo(1);
        for(Objective normalObj : normalObjs) {
            assertThat(normalObj.getPriority().getId()).isEqualTo(priorityOfJohn2.getId());
        }
    }

    @Test
    public void findByObjective_whenParentIsOffered_thenReturnObjectivesHaveSameParent() {
        /* Arrange */
        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").parent(objOfJohn1).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").parent(objOfJohn1).build();
        objectiveRepository.save(objOfJohn3);

        /* Act */
        List<Objective> results = objectiveRepository.findByObjective(Objective.builder().parent(objOfJohn1).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getParent().getId()).isEqualTo(objOfJohn1.getId());
        }
    }

    @Test
    public void findByObjective_whenEndDateAndTitleAreOffered_thenReturnObjectivesHaveSameEndDateAndContainSameTitle() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        Objective objOfJohn1 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJohn1").description("descOfJohn1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("titleOfJohn2").description("descOfJohn2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Objective objOfJohn3 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1)).status(Cell.Status.Prepared).title("titleOfJohn3").description("descOfJohn3").member(john).build();
        objectiveRepository.save(objOfJohn3);

        Objective objOfJulia2 = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Complete).title("titleOfJulia2").description("descOfJulia2").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        /* Act */
        String title = "John";
        List<Objective> results = objectiveRepository.findByObjective(Objective.builder().title(title).endDate(LocalDate.now()).build());

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Objective result : results) {
            assertThat(result.getEndDate()).isBefore(LocalDate.now().plusDays(1));
            assertThat(result.getEndDate()).isAfter(LocalDate.now().minusDays(1));
            assertThat(result.getEndDate()).isEqualTo(LocalDate.now());
            assertThat(result.getTitle().contains(title)).isTrue();
        }
    }
}
package com.kidongyun.bridge.api.repository;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.*;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
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
public class CellRepositoryTest {

    @Autowired
    CellRepository<Cell> cellRepository;

    @Autowired
    CellRepository<Objective> objectiveRepository;

    @Autowired
    CellRepository<Plan> planRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PriorityRepository priorityRepository;

    @Test
    public void save_normalCase_whenItIsNewOne() {
        /* Arrange, Act, Assert */
        Objective obj1 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").build();
        objectiveRepository.save(obj1);
        assertThat(cellRepository.findAll().size()).isEqualTo(1);

        Objective obj2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title2").description("desc2").build();
        objectiveRepository.save(obj2);
        assertThat(cellRepository.findAll().size()).isEqualTo(2);

        Plan plan1 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content3").build();
        planRepository.save(plan1);
        assertThat(cellRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    public void save_normalCase_whenObjectiveIsAlreadyExisted() throws Exception {
        /* Arrange */
        Objective obj = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("before").description("desc1").build();
        objectiveRepository.save(obj);
        assertThat(objectiveRepository.findById(obj.getId()).orElseThrow(Exception::new).getTitle()).isEqualTo("before");

        /* Act */
        obj.setTitle("after");
        objectiveRepository.save(obj);

        /* Assert */
        assertThat(objectiveRepository.findById(obj.getId()).orElseThrow(Exception::new).getTitle()).isEqualTo("after");
    }

    @Test
    public void deleteById_normalCase() {
        /* Arrange */
        Objective obj = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("before").description("desc1").build();
        objectiveRepository.save(obj);
        List<Objective> objectives = objectiveRepository.findAll();
        assertThat(objectives.size()).isEqualTo(1);

        /* Act */
        objectiveRepository.deleteById(obj.getId());
        List<Objective> results = objectiveRepository.findAll();

        /* Assert */
        assertThat(results.size()).isEqualTo(0);
    }

    @Test
    public void findByType_normalCase() {
        /* Arrange */
        Objective obj = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").build();
        objectiveRepository.save(obj);

        Plan plan = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content1").build();
        planRepository.save(plan);

        /* Act */
        Set<Objective> results = objectiveRepository.findByType(Cell.Type.Objective);

        /* Assert */
        assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void findByTypeOrderByStartDateTime_whenItIsNormalCase_thenReturnObjOrderedAsc() {
        /* Arrange */
        Objective obj1 = Objective.builder()
                .startDateTime(LocalDateTime.of(2021,2,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").build();
        objectiveRepository.save(obj1);

        Objective obj2 = Objective.builder()
                .startDateTime(LocalDateTime.of(2021, 1, 20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title2").description("desc2").build();
        objectiveRepository.save(obj2);

        Objective obj3 = Objective.builder()
                .startDateTime(LocalDateTime.of(2022, 10, 20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title3").description("desc3").build();
        objectiveRepository.save(obj3);

        Objective obj4 = Objective.builder()
                .startDateTime(LocalDateTime.of(2019, 5, 20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title4").description("desc4").build();
        objectiveRepository.save(obj4);

        Plan plan1 = Plan.builder().startDateTime(LocalDateTime.of(2020,10,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content1").build();
        planRepository.save(plan1);

        Plan plan2 = Plan.builder().startDateTime(LocalDateTime.of(2019,1,15,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content2").build();
        planRepository.save(plan2);

        /* Act */
        Set<Objective> results = objectiveRepository.findByTypeOrderByStartDateTime(Cell.Type.Objective);

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        Cell prev = Cell.empty();
        boolean first = true;
        for(Cell result : results) {
            if(first) {
                prev = result;
                first = false;
                continue;
            }

            assertThat(result.getStartDateTime()).isAfter(prev.getStartDateTime());
            prev = result;
        }
    }

    @Test
    public void findByTypeOrderByEndDateTime_whenItIsNormalCase_thenReturnObjOrderedAsc() {
        /* Arrange */
        Objective obj1 = Objective.builder()
                .endDateTime(LocalDateTime.of(2021,2,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").build();
        objectiveRepository.save(obj1);

        Objective obj2 = Objective.builder()
                .endDateTime(LocalDateTime.of(2021, 1, 20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title2").description("desc2").build();
        objectiveRepository.save(obj2);

        Objective obj3 = Objective.builder()
                .endDateTime(LocalDateTime.of(2022, 10, 20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title3").description("desc3").build();
        objectiveRepository.save(obj3);

        Objective obj4 = Objective.builder()
                .endDateTime(LocalDateTime.of(2019, 5, 20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title4").description("desc4").build();
        objectiveRepository.save(obj4);

        Plan plan1 = Plan.builder()
                .endDateTime(LocalDateTime.of(2020,10,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content1").build();
        planRepository.save(plan1);

        Plan plan2 = Plan.builder()
                .endDateTime(LocalDateTime.of(2019,1,15,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content2").build();
        planRepository.save(plan2);

        /* Act */
        Set<Objective> results = objectiveRepository.findByTypeOrderByEndDateTime(Cell.Type.Objective);

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        Cell prev = Cell.empty();
        boolean first = true;
        for(Cell result : results) {
            if(first) {
                prev = result;
                first = false;
                continue;
            }

            assertThat(result.getEndDateTime()).isAfter(prev.getEndDateTime());
            prev = result;
        }
    }

    @Test
    public void findByMember_normalCase() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Objective objOfJohn1 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title2").description("desc2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Plan planOfJohn1 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content3").member(john).build();
        planRepository.save(planOfJohn1);

        Plan planOfJohn2 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content4").member(john).build();
        planRepository.save(planOfJohn2);

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();

        Objective objOfJulia1 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title5").description("desc5").member(julia).build();
        objectiveRepository.save(objOfJulia1);

        Objective objOfJulia2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title6").description("desc6").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        Plan planOfJulia1 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content7").member(julia).build();
        planRepository.save(planOfJulia1);

        Plan planOfJulia2 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content8").member(julia).build();
        planRepository.save(planOfJulia2);

        /* Act */
        Set<Cell> results = cellRepository.findByMember(john);

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        for(Cell result : results) {
            assertThat(result.getMember().getEmail()).isEqualTo("john@gmail.com");
        }
    }

    @Test
    public void findByMemberEmail_normalCase() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Objective objOfJohn1 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title2").description("desc2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Plan planOfJohn1 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content3").member(john).build();
        planRepository.save(planOfJohn1);

        Plan planOfJohn2 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content4").member(john).build();
        planRepository.save(planOfJohn2);

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();

        Objective objOfJulia1 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title5").description("desc5").member(julia).build();
        objectiveRepository.save(objOfJulia1);

        Objective objOfJulia2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title6").description("desc6").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        Plan planOfJulia1 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content7").member(julia).build();
        planRepository.save(planOfJulia1);

        Plan planOfJulia2 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content8").member(julia).build();
        planRepository.save(planOfJulia2);

        /* Act */
        Set<Cell> results = cellRepository.findByMemberEmail(john.getEmail());

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        for(Cell result : results) {
            assertThat(result.getMember().getEmail()).isEqualTo("john@gmail.com");
        }
    }

    @Test
    public void findByMemberEmailOrderByStartDateTime_whenItIsNormalCase_thenReturnObjOrderedAsc() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Objective objOfJohn1 = Objective.builder()
                .startDateTime(LocalDateTime.of(2021,2,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder()
                .startDateTime(LocalDateTime.of(2021,1,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title2").description("desc2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Plan planOfJohn1 = Plan.builder()
                .startDateTime(LocalDateTime.of(2021,4,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content3").member(john).build();
        planRepository.save(planOfJohn1);

        Plan planOfJohn2 = Plan.builder()
                .startDateTime(LocalDateTime.of(2021,3,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content4").member(john).build();
        planRepository.save(planOfJohn2);

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();

        Objective objOfJulia1 = Objective.builder()
                .startDateTime(LocalDateTime.of(2021,7,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title5").description("desc5").member(julia).build();
        objectiveRepository.save(objOfJulia1);

        Objective objOfJulia2 = Objective.builder()
                .startDateTime(LocalDateTime.of(2021,6,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title6").description("desc6").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        Plan planOfJulia1 = Plan.builder()
                .startDateTime(LocalDateTime.of(2021,5,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content7").member(julia).build();
        planRepository.save(planOfJulia1);

        Plan planOfJulia2 = Plan.builder()
                .startDateTime(LocalDateTime.of(2021,8,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content8").member(julia).build();
        planRepository.save(planOfJulia2);

        /* Act */
        Set<Cell> results = cellRepository.findByMemberEmailOrderByStartDateTime(john.getEmail());

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        Cell prev = Cell.empty();
        boolean first = true;
        for(Cell result : results) {
            if(first) {
                prev = result;
                first = false;
                continue;
            }

            assertThat(result.getStartDateTime()).isAfter(prev.getStartDateTime());
            prev = result;
        }
    }

    @Test
    public void findByMemberEmailOrderByEndDateTime_whenItIsNormalCase_thenReturnObjOrderedAsc() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Objective objOfJohn1 = Objective.builder()
                .endDateTime(LocalDateTime.of(2022,2,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title1").description("desc1").member(john).build();
        objectiveRepository.save(objOfJohn1);

        Objective objOfJohn2 = Objective.builder()
                .endDateTime(LocalDateTime.of(2022,1,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title2").description("desc2").member(john).build();
        objectiveRepository.save(objOfJohn2);

        Plan planOfJohn1 = Plan.builder()
                .endDateTime(LocalDateTime.of(2020,4,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content3").member(john).build();
        planRepository.save(planOfJohn1);

        Plan planOfJohn2 = Plan.builder()
                .endDateTime(LocalDateTime.of(2020,3,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content4").member(john).build();
        planRepository.save(planOfJohn2);

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();

        Objective objOfJulia1 = Objective.builder()
                .endDateTime(LocalDateTime.of(2022,7,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title5").description("desc5").member(julia).build();
        objectiveRepository.save(objOfJulia1);

        Objective objOfJulia2 = Objective.builder()
                .endDateTime(LocalDateTime.of(2022,6,20,0,0,0))
                .status(Cell.Status.Complete).type(Cell.Type.Objective).title("title6").description("desc6").member(julia).build();
        objectiveRepository.save(objOfJulia2);

        Plan planOfJulia1 = Plan.builder()
                .endDateTime(LocalDateTime.of(2022,5,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content7").member(julia).build();
        planRepository.save(planOfJulia1);

        Plan planOfJulia2 = Plan.builder()
                .endDateTime(LocalDateTime.of(2022,8,20,0,0,0))
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content8").member(julia).build();
        planRepository.save(planOfJulia2);

        /* Act */
        Set<Cell> results = cellRepository.findByMemberEmailOrderByEndDateTime(john.getEmail());

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        Cell prev = Cell.empty();
        boolean first = true;
        for(Cell result : results) {
            if(first) {
                prev = result;
                first = false;
                continue;
            }

            assertThat(result.getEndDateTime()).isAfter(prev.getEndDateTime());
            prev = result;
        }
    }

    @Test
    public void findById_normalCase() throws Exception {
        /* Arrange */
        Objective obj1 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).title("title1").description("desc1").build();
        objectiveRepository.save(obj1);

        Objective obj2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).title("title2").description("desc2").parent(obj1).build();
        objectiveRepository.save(obj2);

        Objective obj3 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).title("title3").description("desc3").parent(obj1).build();
        objectiveRepository.save(obj3);

        Objective obj4 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).title("title4").description("desc4").parent(obj2).build();
        objectiveRepository.save(obj4);

        Plan plan1 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content5").build();
        planRepository.save(plan1);

        Plan plan2 = Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).type(Cell.Type.Plan).content("content6").build();
        planRepository.save(plan2);

        /* Act */
        Objective objective = objectiveRepository.findById(obj2.getId()).orElseThrow(Exception::new);
        Plan plan = planRepository.findById(plan1.getId()).orElseThrow(Exception::new);

        /* Assert */
        assertThat(objective).isNotNull();
        assertThat(objective.getId()).isEqualTo(obj2.getId());
        assertThat(plan).isNotNull();
        assertThat(plan.getId()).isEqualTo(plan1.getId());
    }

    @Test
    public void findByIdAndType_normalCase() throws Exception {
        /* Arrange */
        Objective obj = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Complete).title("title1").description("desc1").build();
        objectiveRepository.save(obj);

        Plan plan = Plan.builder().type(Cell.Type.Plan).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).content("content2").build();
        planRepository.save(plan);

        /* Act, Assert */
        assertThat(objectiveRepository.findByIdAndType(obj.getId(), Cell.Type.Objective).orElseThrow(Exception::new).getId()).isEqualTo(obj.getId());
        assertThat(objectiveRepository.findByIdAndType(-1L, Cell.Type.Objective).orElse(null)).isNull();

        assertThat(planRepository.findByIdAndType(-1L, Cell.Type.Plan).orElse(null)).isNull();
        assertThat(planRepository.findByIdAndType(plan.getId(), Cell.Type.Plan).orElseThrow(Exception::new).getId()).isEqualTo(plan.getId());
    }

    @Test
    public void delete_normalCase() throws Exception {
    }
}
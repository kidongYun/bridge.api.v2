package com.kidongyun.bridge.api.repository;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
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
public class CellRepositoryTest {

    @Autowired
    CellRepository<Cell> cellRepository;

    @Autowired
    CellRepository<Objective> objectiveRepository;

    @Autowired
    CellRepository<Plan> planRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void save_normal() {
        /* Arrange, Act, Assert */
        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").type(Cell.Type.Objective).title("title1").description("desc1").build());
        assertThat(cellRepository.findAll().size()).isEqualTo(1);

        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").type(Cell.Type.Objective).title("title2").description("desc2").build());
        assertThat(cellRepository.findAll().size()).isEqualTo(2);

        planRepository.save(Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content3").build());
        assertThat(cellRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    public void findByType_normal() {
        /* Arrange */
        objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").type(Cell.Type.Objective).title("title1").description("desc1").build());

        planRepository.save(Plan.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content1").build());

        /* Act */
        Set<Objective> results = objectiveRepository.findByType(Cell.Type.Objective);

        /* Assert */
        assertThat(results.size()).isEqualTo(1);
    }

    @Test
    public void findByMember_normal() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        memberRepository.save(john);

        objectiveRepository.save(Objective.builder().id(1L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now()).status("completed").type(Cell.Type.Objective)
                .title("title1").description("desc1").member(john).build());

        objectiveRepository.save(Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now()).status("completed").type(Cell.Type.Objective)
                .title("title2").description("desc2").member(john).build());

        planRepository.save(Plan.builder().id(3L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content3").member(john).build());

        planRepository.save(Plan.builder().id(4L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content4").member(john).build());

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();
        memberRepository.save(julia);

        objectiveRepository.save(Objective.builder().id(5L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now()).status("completed").type(Cell.Type.Objective)
                .title("title5").description("desc5").member(julia).build());

        objectiveRepository.save(Objective.builder().id(6L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now()).status("completed").type(Cell.Type.Objective)
                .title("title6").description("desc6").member(julia).build());

        planRepository.save(Plan.builder().id(7L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content7").member(julia).build());

        planRepository.save(Plan.builder().id(8L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content8").member(julia).build());

        /* Act */
        Set<Cell> results = cellRepository.findByMember(john);

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        for(Cell result : results) {
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

        planRepository.save(Plan.builder().id(5L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content5").build());

        planRepository.save(Plan.builder().id(6L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").type(Cell.Type.Plan).content("content6").build());

        /* Act */
        Objective objective = objectiveRepository.findById(2L).orElseThrow(Exception::new);

        Plan plan = planRepository.findById(5L).orElseThrow(Exception::new);

        /* Assert */
        assertThat(objective).isNotNull();
        assertThat(objective.getId()).isEqualTo(2L);
        assertThat(plan).isNotNull();
        assertThat(plan.getId()).isEqualTo(5L);
    }

    @Test
    public void findByIdAndType_normal() throws Exception {
        /* Arrange */
        objectiveRepository.save(Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status("completed").title("title1").description("desc1").build());

        planRepository.save(Plan.builder().id(2L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status("prepared").content("content2").build());

        /* Act, Assert */
        assertThat(objectiveRepository.findByIdAndType(1L, Cell.Type.Objective).orElseThrow(Exception::new).getId()).isEqualTo(1L);
        assertThat(objectiveRepository.findByIdAndType(2L, Cell.Type.Objective).orElseThrow(Exception::new)).isNull();

        assertThat(planRepository.findByIdAndType(1L, Cell.Type.Plan).orElseThrow(Exception::new)).isNull();
        assertThat(planRepository.findByIdAndType(2L, Cell.Type.Plan).orElseThrow(Exception::new).getId()).isEqualTo(2L);
    }
}
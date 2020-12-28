package com.kidongyun.bridge.api;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestRunner implements ApplicationRunner {
    private final ObjectiveRepository objectiveRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member = Member.builder().email("oriondigestive@gmail.com").password("q1w2e3r4").build();

        memberRepository.save(member);

        Objective  parent_objective = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
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
        log.info("YKD : " + objectives.toString());
    }
}

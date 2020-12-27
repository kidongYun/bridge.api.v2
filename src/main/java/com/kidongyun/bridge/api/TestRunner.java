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
                .status("completed").member(member).title("I would like to become senoir developer")
                .description("I always study the techniques of coding for 3 hours").priority(1).build();

        Objective child_objective = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").member(member).title("child").description("child_description").parent(1L).build();

        Objective child_objective2 = Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").member(member).title("child2").description("child_description2").parent(1L).build();

        objectiveRepository.save(parent_objective);
        objectiveRepository.save(child_objective);
        objectiveRepository.save(child_objective2);

        List<Objective> objectives = objectiveRepository.findByParent(1L);
        System.out.println("YKD : " + objectives.toString());
    }
}

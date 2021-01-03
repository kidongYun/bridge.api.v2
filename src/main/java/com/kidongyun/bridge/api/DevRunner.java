package com.kidongyun.bridge.api;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DevRunner implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final ObjectiveRepository objectiveRepository;
    private final PriorityRepository priorityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        memberRepository.save(john);

        objectiveRepository.save(Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").member(john).build());

        objectiveRepository.save(Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).member(john).build());

        objectiveRepository.save(Objective.builder().id(3L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).member(john).build());

        objectiveRepository.save(Objective.builder().id(4L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(Objective.builder().id(2L).build()).member(john).build());

        Member julia = Member.builder().email("julia@gmail.com").password("q1w2e3r4").build();

        memberRepository.save(julia);

        objectiveRepository.save(Objective.builder().id(5L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title5").description("desc5").parent(Objective.builder().id(3L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().id(6L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title6").description("desc6").parent(Objective.builder().id(3L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().id(7L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title7").description("desc7").parent(Objective.builder().id(4L).build()).member(julia).build());

        objectiveRepository.save(Objective.builder().id(8L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title8").description("desc8").parent(Objective.builder().id(4L).build()).member(julia).build());
    }
}

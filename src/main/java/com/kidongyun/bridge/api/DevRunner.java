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
import java.util.Set;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DevRunner implements ApplicationRunner {
    private final ObjectiveRepository objectiveRepository;
    private final PriorityRepository priorityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priorityJohn1 = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priorityJohn1);

        Priority priorityJohn2 = Priority.builder().level(2).description("Normal").member(john).build();
        priorityRepository.save(priorityJohn2);

        Priority priorityJohn3 = Priority.builder().level(3).description("UnImportant").member(john).build();
        priorityRepository.save(priorityJohn3);

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        Priority priorityJulia1 = Priority.builder().level(1).description("Important").member(julia).build();
        priorityRepository.save(priorityJulia1);

        Priority priorityJulia2 = Priority.builder().level(2).description("Normal").member(julia).build();
        priorityRepository.save(priorityJulia2);

        Priority priorityJulia3 = Priority.builder().level(3).description("UnImportant").member(julia).build();
        priorityRepository.save(priorityJulia3);

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
                .status("prepared").title("title7").description("desc7").parent(objectiveJulia2).member(julia).priority(priorityJohn2).build();
        objectiveRepository.save(objectiveJulia3);

        Objective objectiveJulia4 = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title8").description("desc8").parent(objectiveJulia3).member(julia).priority(priorityJulia2).build();
        objectiveRepository.save(objectiveJulia4);
    }
}

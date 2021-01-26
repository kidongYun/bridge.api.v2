package com.kidongyun.bridge.api;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import com.kidongyun.bridge.api.repository.plan.PlanRepository;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.DefaultMessageCodesResolver;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DevRunner implements ApplicationRunner {
    private final ObjectiveRepository objectiveRepository;
    private final PlanRepository planRepository;
    private final PriorityRepository priorityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Member john = Member.builder().email("john@gmail.com").password(encoder.encode("q1w2e3r4")).roles(List.of("USER")).build();

        Priority priorityJohn1 = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priorityJohn1);

        Priority priorityJohn2 = Priority.builder().level(2).description("Normal").member(john).build();
        priorityRepository.save(priorityJohn2);

        Priority priorityJohn3 = Priority.builder().level(3).description("UnImportant").member(john).build();
        priorityRepository.save(priorityJohn3);

        Member julia = Member.builder().email("julia@gmail.com").password(encoder.encode("julia123")).roles(List.of("USER")).build();

        Priority priorityJulia1 = Priority.builder().level(1).description("Important").member(julia).build();
        priorityRepository.save(priorityJulia1);

        Priority priorityJulia2 = Priority.builder().level(2).description("Normal").member(julia).build();
        priorityRepository.save(priorityJulia2);

        Priority priorityJulia3 = Priority.builder().level(3).description("UnImportant").member(julia).build();
        priorityRepository.save(priorityJulia3);

        Objective objectiveJohn1 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021,1,20, 0,0,0))
                .endDateTime(LocalDateTime.of(2022, 1, 20, 0,0,0))
                .status(Cell.Status.Complete).title("I would like to be a CTO in really big company like Google").description("I would like to be a CTO in really big company like Google. I would like to be a CTO in really big company like Google").member(john).priority(priorityJohn1).build();
        objectiveRepository.save(objectiveJohn1);

        Objective objectiveJohn2 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021, 2, 20, 0,0,0))
                .endDateTime(LocalDateTime.of(2022,2,20,0,0,0))
                .status(Cell.Status.Prepared).title("I would like to travel all over the world for my life time").description("I would like to travel all over the world for my life time. I would like to travel all over the world for my life time").parent(objectiveJohn1).member(john).priority(priorityJohn1).build();
        objectiveRepository.save(objectiveJohn2);

        Objective objectiveJohn3 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021,3,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,3,20,0,0,0))
                .status(Cell.Status.Prepared).title("I would like to make some service which could give convenience to people").description("I would like to make some service which could give convenience to people. I would like to make some service which could give convenience to people").parent(objectiveJohn1).member(john).priority(priorityJohn2).build();
        objectiveRepository.save(objectiveJohn3);

        Objective objectiveJohn4 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021,4,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,4,20,0,0,0))
                .status(Cell.Status.Prepared).title("I would like to read the science book enough until i could reach the truth in the world").description("I would like to read the science book enough until i could reach the truth in the world. I would like to read the science book enough until i could reach the truth in the world").parent(objectiveJohn2).member(john).priority(priorityJohn3).build();
        objectiveRepository.save(objectiveJohn4);

        Objective objectiveJulia1 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021,5,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,5,20,0,0,0))
                .status(Cell.Status.Complete).title("title5").description("desc5").member(julia).priority(priorityJulia1).build();
        objectiveRepository.save(objectiveJulia1);

        Objective objectiveJulia2 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021,6,20,0,0,0))
                .endDateTime(LocalDateTime.of(2020,6,20,0,0,0))
                .status(Cell.Status.Prepared).title("title6").description("desc6").parent(objectiveJulia1).member(julia).priority(priorityJulia2).build();
        objectiveRepository.save(objectiveJulia2);

        Objective objectiveJulia3 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021,7,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,7,20,0,0,0))
                .status(Cell.Status.Prepared).title("title7").description("desc7").parent(objectiveJulia2).member(julia).priority(priorityJohn2).build();
        objectiveRepository.save(objectiveJulia3);

        Objective objectiveJulia4 = Objective.builder().type(Cell.Type.Objective)
                .startDateTime(LocalDateTime.of(2021,8,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,8,20,0,0,0))
                .status(Cell.Status.Prepared).title("title8").description("desc8").parent(objectiveJulia3).member(julia).priority(priorityJulia2).build();
        objectiveRepository.save(objectiveJulia4);

        Plan planJohn1 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,1,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,1,20,0,0,0))
                .status(Cell.Status.Prepared).content("content1").member(john).objective(objectiveJohn1).build();
        planRepository.save(planJohn1);

        Plan planJohn2 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,2,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,2,20,0,0,0))
                .status(Cell.Status.Prepared).content("content2").member(john).objective(objectiveJohn1).build();
        planRepository.save(planJohn2);

        Plan planJohn3 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,3,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,3,20,0,0,0))
                .status(Cell.Status.Prepared).content("content3").member(john).objective(objectiveJohn2).build();
        planRepository.save(planJohn3);
        Plan planJohn4 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,4,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,4,20,0,0,0))
                .status(Cell.Status.Prepared).content("content4").member(john).objective(objectiveJohn2).build();
        planRepository.save(planJohn4);

        Plan planJohn5 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,5,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,5,20,0,0,0))
                .status(Cell.Status.Prepared).content("content5").member(john).objective(objectiveJohn3).build();
        planRepository.save(planJohn5);

        Plan planJohn6 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,6,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,6,20,0,0,0))
                .status(Cell.Status.Prepared).content("content6").member(john).objective(objectiveJohn3).build();
        planRepository.save(planJohn6);

        Plan planJohn7 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,7,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,7,20,0,0,0))
                .status(Cell.Status.Prepared).content("content7").member(john).objective(objectiveJohn4).build();
        planRepository.save(planJohn7);

        Plan planJohn8 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,8,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,8,20,0,0,0))
                .status(Cell.Status.Prepared).content("content8").member(john).objective(objectiveJohn4).build();
        planRepository.save(planJohn8);

        Plan planJulia1 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,1,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,1,20,0,0,0))
                .status(Cell.Status.Prepared).content("content9").member(julia).objective(objectiveJulia1).build();
        planRepository.save(planJulia1);

        Plan planJulia2 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,2,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,2,20,0,0,0))
                .status(Cell.Status.Prepared).content("content10").member(julia).objective(objectiveJulia1).build();
        planRepository.save(planJulia2);

        Plan planJulia3 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,3,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,3,20,0,0,0))
                .status(Cell.Status.Prepared).content("content11").member(julia).objective(objectiveJulia2).build();
        planRepository.save(planJulia3);

        Plan planJulia4 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,4,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,4,20,0,0,0))
                .status(Cell.Status.Prepared).content("content12").member(julia).objective(objectiveJulia2).build();
        planRepository.save(planJulia4);

        Plan planJulia5 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,5,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,5,20,0,0,0))
                .status(Cell.Status.Prepared).content("content13").member(julia).objective(objectiveJulia3).build();
        planRepository.save(planJulia5);

        Plan planJulia6 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,6,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,6,20,0,0,0))
                .status(Cell.Status.Prepared).content("content14").member(julia).objective(objectiveJulia3).build();
        planRepository.save(planJulia6);

        Plan planJulia7 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,7,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,7,20,0,0,0))
                .status(Cell.Status.Prepared).content("content15").member(julia).objective(objectiveJulia4).build();
        planRepository.save(planJulia7);

        Plan planJulia8 = Plan.builder().type(Cell.Type.Plan)
                .startDateTime(LocalDateTime.of(2021,8,20,0,0,0))
                .endDateTime(LocalDateTime.of(2022,8,20,0,0,0))
                .status(Cell.Status.Prepared).content("content16").member(julia).objective(objectiveJulia4).build();
        planRepository.save(planJulia8);
    }
}

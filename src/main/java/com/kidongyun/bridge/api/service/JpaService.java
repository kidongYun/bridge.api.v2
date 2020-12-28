package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import com.kidongyun.bridge.api.repository.plan.PlanRepository;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import com.kidongyun.bridge.api.repository.todo.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaService {
    private final ObjectiveRepository objectiveRepository;
    private final PlanRepository planRepository;
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final PriorityRepository priorityRepository;

    public void save() {

    }
}

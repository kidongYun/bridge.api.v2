package com.kidongyun.bridge.api;

import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class TestRunner implements ApplicationRunner {
    private final ObjectiveRepository objectiveRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}

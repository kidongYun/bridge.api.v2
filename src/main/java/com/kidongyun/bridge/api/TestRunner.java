package com.kidongyun.bridge.api;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestRunner implements ApplicationRunner {
    private final CellRepository<Objective> objectiveRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Member member = Member.builder().email("oriondigestive@gmail.com").password("q1w2e3r4").build();

        memberRepository.save(member);

        Objective objective = Objective.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now()).status("completed").member(member)
                .title("I would like to become senoir developer").description("I always study the techniques of coding for 3 hours").priority(1).build();

        objectiveRepository.save(objective);
    }
}

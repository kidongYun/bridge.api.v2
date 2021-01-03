package com.kidongyun.bridge.api.repository.member;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PriorityRepository priorityRepository;

    @Test
    @Transactional
    public void findByEmail_normal() {
        /* Arrange */
        memberRepository.save(Member.builder().email("john@gmail.com").password("q1w2e3r4").build());
        memberRepository.save(Member.builder().email("julia@gmail.com").password("julia123").build());

        /* Act */
        Member member = memberRepository.findByEmail("john@gmail.com")
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        /* Assert */
        assertThat(member.getEmail()).isEqualTo("john@gmail.com");
        assertThat(member.getPassword()).isEqualTo("q1w2e3r4");
    }

    @Test
    @Transactional
    public void findByEmail_prioritiesShouldBeNormal() {
        /* Arrange */
        Priority priority1 = Priority.builder().level(1).description("Important").build();
        priorityRepository.save(priority1);
        Priority priority2 = Priority.builder().level(2).description("Normal").build();
        priorityRepository.save(priority2);
        Priority priority3 = Priority.builder().level(3).description("Unimportant").build();
        priorityRepository.save(priority3);
        Set<Priority> priorities = Set.of(priority1, priority2, priority3);

        memberRepository.save(Member.builder().email("john@gmail.com").password("q1w2e3r4").priorities(priorities).build());

        /* Act */
        Member member = memberRepository.findByEmail("john@gmail.com")
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        /* Assert */
        assertThat(member.getEmail()).isEqualTo("john@gmail.com");
        assertThat(member.getPassword()).isEqualTo("q1w2e3r4");
        assertThat(member.getPriorities().size()).isEqualTo(3);
    }
}
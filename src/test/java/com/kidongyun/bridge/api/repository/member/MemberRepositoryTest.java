package com.kidongyun.bridge.api.repository.member;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PriorityRepository priorityRepository;

    @Test
    public void save_normalCase() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        Member resultJohn = memberRepository.save(john);
        Member resultJulia = memberRepository.save(julia);
        List<Member> results = memberRepository.findAll();

        assertThat(results.size()).isEqualTo(2);
        assertThat(resultJohn.getEmail()).isEqualTo(john.getEmail());
        assertThat(resultJulia.getEmail()).isEqualTo(julia.getEmail());
    }

    @Test
    public void existsById_normalCase() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        memberRepository.save(john);

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();

        /* Act */
        boolean resultJohn = memberRepository.existsById(john.getEmail());
        boolean resultJulia = memberRepository.existsById(julia.getEmail());

        /* Assert */
        assertThat(resultJohn).isEqualTo(true);
        assertThat(resultJulia).isEqualTo(false);
    }

    @Test
    public void findByEmail_normalCase() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        memberRepository.save(john);

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();
        memberRepository.save(julia);

        /* Act */
        Member member = memberRepository.findByEmail(john.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        /* Assert */
        assertThat(member.getEmail()).isEqualTo(john.getEmail());
        assertThat(member.getPassword()).isEqualTo(john.getPassword());
    }

    @Test
    public void findByEmail_prioritiesShouldBeNormal() {
        /* Arrange */
        Priority priority1 = Priority.builder().level(1).description("Important").build();
        priorityRepository.save(priority1);

        Priority priority2 = Priority.builder().level(2).description("Normal").build();
        priorityRepository.save(priority2);

        Priority priority3 = Priority.builder().level(3).description("Unimportant").build();
        priorityRepository.save(priority3);

        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").priorities(Set.of(priority1, priority2, priority3)).build();
        memberRepository.save(john);

        /* Act */
        Member member = memberRepository.findByEmail(john.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        /* Assert */
        assertThat(member.getEmail()).isEqualTo(john.getEmail());
        assertThat(member.getPassword()).isEqualTo(john.getPassword());
        assertThat(member.getPriorities().size()).isEqualTo(3);
    }
}
package com.kidongyun.bridge.api.repository.priority;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Priority;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class PriorityRepositoryTest {
    @Autowired
    PriorityRepository priorityRepository;

    @Test
    public void findByIdAndMember_normal() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        Priority priority = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priority);

        /* Act */
        Priority result1 = priorityRepository.findByIdAndMember(1L, john).orElseThrow(Exception::new);
        Priority result2 = priorityRepository.findByIdAndMember(1L, Member.builder().email("john@gmail.com").build()).orElseThrow(Exception::new);

        /* Assert */
        assertThat(result1.getId()).isEqualTo(1L);
        assertThat(result1.getLevel()).isEqualTo(1);
        assertThat(result1.getDescription()).isEqualTo("Important");
        assertThat(result2.getId()).isEqualTo(1L);
        assertThat(result2.getLevel()).isEqualTo(1);
        assertThat(result2.getDescription()).isEqualTo("Important");
    }

    @Test
    public void findByIdAndMemberEmail_normal() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        Priority priority = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priority);

        /* Act */
        Priority result = priorityRepository.findByIdAndMemberEmail(priority.getId(), "john@gmail.com").orElseThrow(Exception::new);

        /* Assert */
        assertThat(result.getId()).isEqualTo(priority.getId());
        assertThat(result.getLevel()).isEqualTo(1);
        assertThat(result.getDescription()).isEqualTo("Important");
    }
}
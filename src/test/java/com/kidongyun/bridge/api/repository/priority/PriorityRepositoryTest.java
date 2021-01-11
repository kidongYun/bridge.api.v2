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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QuerydslConfig.class)
public class PriorityRepositoryTest {
    @Autowired
    PriorityRepository priorityRepository;

    @Test
    public void findByIdAndMember_normalCase() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        Priority priority = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priority);

        /* Act */
        Priority result = priorityRepository.findByIdAndMember(priority.getId(), john).orElseThrow(Exception::new);

        /* Assert */
        assertThat(result.getId()).isEqualTo(priority.getId());
        assertThat(result.getLevel()).isEqualTo(priority.getLevel());
        assertThat(result.getDescription()).isEqualTo(priority.getDescription());
    }

    @Test
    public void findByIdAndMemberEmail_normalCase() throws Exception {
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

    @Test
    public void findByMember_normalCase() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        Priority priorityOfJohn = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priorityOfJohn);

        Member julia = Member.builder().email("julia@gmail.com").password("julia123").build();
        Priority priorityOfJulia = Priority.builder().level(2).description("Normal").member(julia).build();
        priorityRepository.save(priorityOfJulia);

        /* Act */
        Priority result = priorityRepository.findByMember(john).orElseThrow(Exception::new);

        /* Assert */
        assertThat(result.getId()).isEqualTo(priorityOfJohn.getId());
        assertThat(result.getLevel()).isEqualTo(priorityOfJohn.getLevel());
        assertThat(result.getDescription()).isEqualTo(priorityOfJohn.getDescription());
    }

    @Test
    public void findByMemberEmail_normalCase() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priorityOfJohn1 = Priority.builder().level(1).description("Important").member(john).build();
        priorityRepository.save(priorityOfJohn1);

        Priority priorityOfJohn2 = Priority.builder().level(2).description("Normal").member(john).build();
        priorityRepository.save(priorityOfJohn2);

        Priority priorityOfJohn3 = Priority.builder().level(3).description("Unimportant").member(john).build();
        priorityRepository.save(priorityOfJohn3);

        /* Act */
        Set<Priority> results = priorityRepository.findByMemberEmail(john.getEmail());

        /* Assert */
        assertThat(results.size()).isEqualTo(3);
        for(Priority result : results) {
            assertThat(result.getMember().getEmail()).isEqualTo(john.getEmail());
        }
    }
}
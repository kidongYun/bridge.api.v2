package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class PriorityServiceTest {
    @Mock
    PriorityRepository priorityRepository;
    @InjectMocks
    PriorityService priorityServiceMock;
    @Autowired
    PriorityService priorityService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = HttpClientErrorException.class)
    public void findByMemberEmail_emailIsNull() throws Exception {
        /* Arrange, Act, Assert */
        priorityService.findByMemberEmail(null);
    }

    @Test
    public void findByMemberEmail_normalCase() throws Exception {
        /* Arrange */
        Priority stub = Priority.builder().description("findByMemberEmail").build();
        when(priorityRepository.findByMemberEmail(anyString())).thenReturn(Set.of(stub));

        /* Act */
        Set<Priority> results = priorityServiceMock.findByMemberEmail(anyString());

        /* Assert */
        assertThat(results.size()).isEqualTo(1);
        for(Priority result : results) {
            assertThat(result.getDescription()).isEqualTo(stub.getDescription());
        }
    }

    @Test
    public void findByIdAndMemberEmail_whempPriorityIdAndEmailAreNull_thenReturnOptionalEmpty() {
        /* Arrange, Act */
        Optional<Priority> result = priorityService.findByIdAndMemberEmail(null, null);

        /* Assert */
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void findByIdAndMemberEmail_whenPriorityIdIsNotNull_thenEmailIsNull() throws Exception {
        /* Arrange */
        Priority stub = Priority.builder().description("findById").build();
        when(priorityRepository.findById(anyLong())).thenReturn(Optional.of(stub));

        /* Act */
        Priority result = priorityServiceMock.findByIdAndMemberEmail(anyLong(), null)
                .orElseThrow(Exception::new);

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }

    @Test
    public void findByIdAndMemberEmail_whenPriorityIdIsNull_thenEmailIsNotNull() throws Exception {
        /* Arrange */
        Priority stub = Priority.builder().description("findByMemberEmail").build();
        when(priorityRepository.findByMemberEmail(anyString())).thenReturn(Set.of(stub));

        /* Act */
        Priority result = priorityServiceMock.findByIdAndMemberEmail(null, anyString())
                .orElseThrow(Exception::new);

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }

    @Test
    public void findByIdAndMemberEmail_priorityIdAndEmailAreNotNull() throws Exception {
        /* Arrange */
        Priority stub = Priority.builder().description("findByIdAndMemberEmail").build();
        when(priorityRepository.findByIdAndMemberEmail(anyLong(), anyString())).thenReturn(Optional.of(stub));

        /* Act */
        Priority result = priorityServiceMock.findByIdAndMemberEmail(anyLong(), anyString())
                .orElseThrow(Exception::new);

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }
}
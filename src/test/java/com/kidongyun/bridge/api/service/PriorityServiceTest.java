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
    public void find_priorityIdAndEmailAreNull() throws Exception {
        /* Arrange, Act, Assert */
        priorityService.findByIdAndMemberEmail(null, null);
    }

    @Test
    public void find_priorityIdIsNotNull_EmailIsNull() throws Exception {
        /* Arrange */
        Priority stub = Priority.builder().description("findById").build();
        when(priorityRepository.findById(anyLong())).thenReturn(Optional.of(stub));

        /* Act */
        Priority result = priorityServiceMock.findByIdAndMemberEmail(anyLong(), null);

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }

    @Test
    public void find_priorityIdIsNull_EmailIsNotNull() throws Exception {
        /* Arrange */
        Priority stub = Priority.builder().description("findByMemberEmail").build();
        when(priorityRepository.findByMemberEmail(anyString())).thenReturn(Optional.of(stub));

        /* Act */
        Priority result = priorityServiceMock.findByIdAndMemberEmail(null, anyString());

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }

    @Test
    public void find_priorityIdAndEmailAreNotNull() throws Exception {
        /* Arrange */
        Priority stub = Priority.builder().description("findByIdAndMemberEmail").build();
        when(priorityRepository.findByIdAndMemberEmail(anyLong(), anyString())).thenReturn(Optional.of(stub));

        /* Act */
        Priority result = priorityServiceMock.findByIdAndMemberEmail(anyLong(), anyString());

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }
}
package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
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
public class CellServiceTest {
    @Mock
    CellRepository cellRepositoryMock;
    @InjectMocks
    CellService cellServiceMock;
    @Autowired
    CellService cellService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = HttpClientErrorException.class)
    public void findByType_typeIsNull() {
        /* Arrange, Act, Assert */
        cellService.findByType(null);
    }

    @Test
    @SuppressWarnings(value = "unchecked")
    public void findByType_objective_normalCase() {
        /* Arrange */
        Objective stub = Objective.builder().description("findByType").build();
        when(cellRepositoryMock.findByType(Cell.Type.Objective)).thenReturn(Set.of(stub));

        /* Act */
        Set<Objective> results = cellServiceMock.findByType(Cell.Type.Objective);

        /* Assert */
        for(Objective result : results) {
            assertThat(result.getDescription()).isEqualTo(stub.getDescription());
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void findById_idIsNull() throws Exception {
        /* Arrange, Act, Assert */
        cellService.findById(null);
    }

    @Test
    @SuppressWarnings(value = "unchecked")
    public void findById_objective_normalCase() throws Exception {
        /* Arrange */
        Objective stub = Objective.builder().description("findByType").build();
        when(cellRepositoryMock.findById(anyLong())).thenReturn(Optional.of(stub));

        /* Act */
        Objective result = (Objective) cellServiceMock.findById(anyLong());

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }

    @Test(expected = HttpClientErrorException.class)
    public void findByMemberEmail_emailIsNull() throws Exception {
        /* Arrange, Act, Assert */
        cellService.findByMemberEmail(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByMemberEmail_normalCase() {
        /* Arrange */
        Objective stub = Objective.builder().description("findByMemberEmail").build();
        when(cellRepositoryMock.findByMemberEmail(anyString())).thenReturn(Set.of(stub));

        /* Act */
        Set<Objective> results = cellServiceMock.findByMemberEmail(anyString());

        /* Assert */
        for(Objective result : results) {
            assertThat(result.getDescription()).isEqualTo(stub.getDescription());
        }
    }
}
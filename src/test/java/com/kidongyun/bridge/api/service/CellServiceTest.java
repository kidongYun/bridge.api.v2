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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    CellService<Objective> objectiveService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByType_typeIsNull() {
        /* Arrange, Act */
        Set<Cell> result = cellService.findByType(null);

        /* Assert */
        assertThat(result.isEmpty()).isTrue();
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

    @Test
    @SuppressWarnings("unchecked")
    public void findById_idIsNull() throws Exception {
        /* Arrange, Act */
        Optional<Cell> result = cellService.findById(null);

        /* Assert */
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @SuppressWarnings(value = "unchecked")
    public void findById_objective_normalCase() throws Throwable {
        /* Arrange */
        Objective stub = Objective.builder().description("findByType").build();
        when(cellRepositoryMock.findById(anyLong())).thenReturn(Optional.of(stub));

        /* Act */
        Objective result = (Objective) cellServiceMock.findById(anyLong()).orElseThrow(Exception::new);

        /* Assert */
        assertThat(result.getDescription()).isEqualTo(stub.getDescription());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByMemberEmail_emailIsNull() throws Exception {
        /* Arrange, Act */
        Set<Cell> result = cellService.findByMemberEmail(null);

        /* Assert */
        assertThat(result.isEmpty()).isTrue();
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

    @Test
    public void order_whenCriteriaIsId_thenReturnOrderedSetBasedId() {
        /* Arrange */
        Set<Objective> stub = Set.of(
                Objective.builder().id(2L).build(),
                Objective.builder().id(4L).build(),
                Objective.builder().id(3L).build(),
                Objective.builder().id(1L).build()
        );

        /* Act */
        Set<Objective> results = objectiveService.order(stub, Comparator.comparingLong(Objective::getId));

        /* Assert */
        assertThat(results.size()).isEqualTo(4);

        Objective prev = null;
        for(Objective result : results) {
            if(Objects.isNull(prev)) {
                prev = result;
                continue;
            }

            assertThat(prev.getId()).isLessThan(result.getId());
            prev = result;
        }
    }

    @Test
    public void order_whenCriteriaIsStartDate_thenReturnOrderedSetBasedStartDate() {
        /* Arrange */
        Set<Objective> stub = Set.of(
                Objective.builder().id(1L).startDate(LocalDate.of(2021,7,20)).build(),
                Objective.builder().id(2L).startDate(LocalDate.of(2022,6,12)).build(),
                Objective.builder().id(3L).startDate(LocalDate.of(2020,4,2)).build(),
                Objective.builder().id(4L).startDate(LocalDate.of(2019,7,7)).build()
        );

        /* Act */
        Set<Objective> results = objectiveService.order(stub, Comparator.comparing(Objective::getStartDate));

        /* Assert */
        assertThat(results.size()).isEqualTo(4);

        Objective prev = null;
        for(Objective result : results) {
            if(Objects.isNull(prev)) {
                prev = result;
                continue;
            }

            assertThat(prev.getStartDate()).isBefore(result.getStartDate());
            prev = result;
        }
    }

    @Test
    public void order_whenCriteriaIsId_thenReturnOrderedListBasedId() {
        /* Arrange */
        List<Objective> stub = List.of(
                Objective.builder().id(2L).build(),
                Objective.builder().id(4L).build(),
                Objective.builder().id(3L).build(),
                Objective.builder().id(1L).build()
        );

        /* Act */
        List<Objective> results = objectiveService.order(stub, Comparator.comparingLong(Objective::getId));

        /* Assert */
        assertThat(results.size()).isEqualTo(4);

            Objective prev = null;
            for(Objective result : results) {
                if(Objects.isNull(prev)) {
                    prev = result;
                    continue;
                }

                assertThat(prev.getId()).isLessThan(result.getId());
                prev = result;
            }
    }

    @Test
    public void order_whenCriteriaIsStartDate_thenReturnOrderedListBasedStartDate() {
        /* Arrange */
        List<Objective> stub = List.of(
                Objective.builder().id(1L).startDate(LocalDate.of(2021,7,20)).build(),
                Objective.builder().id(2L).startDate(LocalDate.of(2022,6,12)).build(),
                Objective.builder().id(3L).startDate(LocalDate.of(2020,4,2)).build(),
                Objective.builder().id(4L).startDate(LocalDate.of(2019,7,7)).build()
        );

        /* Act */
        List<Objective> results = objectiveService.order(stub, Comparator.comparing(Objective::getStartDate));

        /* Assert */
        assertThat(results.size()).isEqualTo(4);

        Objective prev = null;
        for(Objective result : results) {
            if(Objects.isNull(prev)) {
                prev = result;
                continue;
            }

            assertThat(prev.getStartDate()).isBefore(result.getStartDate());
            prev = result;
        }
    }
}
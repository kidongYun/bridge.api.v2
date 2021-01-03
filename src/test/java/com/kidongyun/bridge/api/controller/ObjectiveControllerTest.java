package com.kidongyun.bridge.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class ObjectiveControllerTest {
    @Mock
    ObjectiveRepository objectiveRepository;

    @InjectMocks
    ObjectiveController objectiveController;

    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(objectiveController).build();
    }

    @Test
    public void getObjective_normal() throws Exception {
        /* Arrange */
        Set<Objective> stub = new HashSet<>();

        Objective parent = Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title1").description("desc1").member(Member.builder().build()).build();

        stub.add(parent);
        stub.add(Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").member(Member.builder().build()).parent(parent).build());

        when(objectiveRepository.findByType(Cell.Type.Objective)).thenReturn(stub);

        mockMvc.perform(get("/api/v1/objective"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getObjectiveById_normal() throws Exception {
        /* Arrange */
        when(objectiveRepository.findByIdAndType(2L, Cell.Type.Objective)).thenReturn(Optional.of(Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).build()));

        /* Act, Assert */
        mockMvc.perform(get("/api/v1/objective/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test(expected = Exception.class)
    public void getObjectiveById_cantFindObjective() throws Exception {
        /* Arrange */
        when(objectiveRepository.findById(2L)).thenReturn(Optional.empty());

        /* Act, Assert */
        mockMvc.perform(get("/api/v1/objective/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void postObjective_normal() throws Exception {
        /* Arrange */
        Objective.Post stub = Objective.Post.builder().endDateTime(LocalDateTime.of(2021, 6, 21, 5, 30))
                .status("completed").email("john@gmail.com").title("title from test").description("desc from test").build();
        String content = objectMapper.writeValueAsString(stub);

        when(objectiveRepository.save(stub.toDomain())).thenReturn(stub.toDomain());

        /* Act, Assert */
        mockMvc.perform(post("/api/v1/objective")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
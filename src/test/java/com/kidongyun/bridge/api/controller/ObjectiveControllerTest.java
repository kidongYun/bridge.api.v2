package com.kidongyun.bridge.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.exception.Advice;
import com.kidongyun.bridge.api.service.MemberService;
import com.kidongyun.bridge.api.service.ObjectiveService;
import com.kidongyun.bridge.api.service.PriorityService;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class ObjectiveControllerTest {
    @Mock
    private ObjectiveService objectiveService;
    @Mock
    private PriorityService priorityService;
    @Mock
    private MemberService memberService;
    @InjectMocks
    private ObjectiveController objectiveControllerMock;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Advice advice;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(objectiveControllerMock)
                .setControllerAdvice(advice)
                .build();
    }

    @Test
    public void getObjective_normal() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priority = Priority.builder().id(1L).level(1).description("Important").member(john).build();

        Objective parent = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).title("title1").description("desc1").member(john).priority(priority).build();

        Objective child = Objective.builder().type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).title("title2").description("desc2").member(john).priority(priority).parent(parent).build();

        Set<Objective> stub = Set.of(parent, child);

        when(objectiveService.findByType(Cell.Type.Objective)).thenReturn(stub);

        /* Act, Assert */
        mockMvc.perform(get("/api/v1/objective")
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getObjectiveById_normal() throws Exception {
        /* Arrange */
        Objective stub = Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("title2").description("desc2").parent(Objective.builder().id(1L).build())
                .priority(Priority.builder().id(1L).build()).member(Member.builder().email("john@gmail.com").build()).build();

        when(objectiveService.findById(anyLong())).thenReturn(Optional.of(stub));

        /* Act, Assert */
        mockMvc.perform(get("/api/v1/objective/email/2")
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getObjectiveById_cannotFindObjective() throws Exception {
        /* Arrange */
        when(objectiveService.findById(anyLong())).thenReturn(null);

        /* Act, Assert */
        String response = mockMvc.perform(get("/api/v1/objective/id/2")
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertThat(response).isEqualTo("'obj', 'obj.member', 'obj.priority' must not be null");
    }

    @Test
    public void postObjective_normalCase() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priority = Priority.builder().id(1L).level(1).description("Important").member(john).build();

        Objective parent = Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).title("title1").description("desc1").member(john).priority(priority).build();

        Objective.Post stub = Objective.Post.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).email(john.getEmail()).title("title from test").description("desc from test").priorityId(priority.getId()).parentId(parent.getId()).build();

        String content = objectMapper.writeValueAsString(stub);

        when(priorityService.findByIdAndMemberEmail(anyLong(), anyString())).thenReturn(priority);
        when(objectiveService.findById(anyLong())).thenReturn(Optional.of(parent));
        when(memberService.findByEmail(anyString())).thenReturn(Optional.of(john));
        when(objectiveService.save(any(Objective.class))).thenReturn(Optional.of(Objective.of(stub, priority, john, parent)));

        /* Act, Assert */
        mockMvc.perform(post("/api/v1/objective")
                .characterEncoding("utf-8")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void putObjective_normalCase() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priority = Priority.builder().id(1L).level(1).description("Important").member(john).build();

        Objective parent = Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Prepared).title("title1").description("desc1").member(john).priority(priority).build();

        Objective.Put stub = Objective.Put.builder().id(2L).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status(Cell.Status.Complete).email(john.getEmail()).title("title from test").description("desc from test").priorityId(priority.getId()).parentId(parent.getId()).build();

        String content = objectMapper.writeValueAsString(stub);

        when(priorityService.findByIdAndMemberEmail(anyLong(), anyString())).thenReturn(priority);
        when(objectiveService.findById(anyLong())).thenReturn(Optional.of(parent));
        when(memberService.findByEmail(anyString())).thenReturn(Optional.of(john));
        when(objectiveService.save(any(Objective.class))).thenReturn(Optional.of(Objective.of(stub, priority, john, parent)));

        /* Act, Assert */
        mockMvc.perform(put("/api/v1/objective")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
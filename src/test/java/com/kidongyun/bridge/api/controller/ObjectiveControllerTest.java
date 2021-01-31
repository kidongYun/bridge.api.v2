package com.kidongyun.bridge.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidongyun.bridge.api.config.SecurityConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.exception.Advice;
import com.kidongyun.bridge.api.security.TokenProvider;
import com.kidongyun.bridge.api.service.MemberService;
import com.kidongyun.bridge.api.service.ObjectiveService;
import com.kidongyun.bridge.api.service.PriorityService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ObjectiveController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ObjectiveControllerTest {
    @MockBean
    private SecurityConfig securityConfigMock;
    @MockBean
    private TokenProvider tokenProviderMock;
    @MockBean
    private ObjectiveService objectiveServiceMock;
    @MockBean
    private PriorityService priorityServiceMock;
    @MockBean
    private MemberService memberServiceMock;
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
    public void getObjective_whenParameterHasId_thenReturnNormalResponse() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priority = Priority.builder().id(1L).level(1).description("Important").member(john).build();

        Objective parent = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Prepared).title("title1").description("desc1").member(john).priority(priority).build();

        Objective child = Objective.builder().type(Cell.Type.Objective).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Prepared).title("title2").description("desc2").member(john).priority(priority).parent(parent).build();

        Set<Objective> stub = Set.of(parent, child);

        when(priorityServiceMock.findByIdAndMemberEmail(anyLong(), anyString())).thenReturn(null);
        when(memberServiceMock.findByEmail(anyString())).thenReturn(null);
        when(objectiveServiceMock.findByType(Cell.Type.Objective)).thenReturn(stub);

        /* Act, Assert */
        mockMvc.perform(get("/api/v1/objective")
                .param("id", "5")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getObjective_whenParameterHasEmail_thenReturnNormalResponse() throws Exception {
        /* Arrange */
        Objective stub = Objective.builder().id(2L).type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("title2").description("desc2").parent(Objective.builder().id(1L).build())
                .priority(Priority.builder().id(1L).build()).member(Member.builder().email("john@gmail.com").build()).build();

        when(objectiveServiceMock.findByObjective(any(Objective.class))).thenReturn(List.of(stub));

        /* Act, Assert */
        mockMvc.perform(get("/api/v1/objective")
                .param("email", "john@gmail.com")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getObjective_whenObjectiveGetIsNull_thenReturnObjectivesWithoutFilter() throws Exception {
        /* Arrange, Act */
        String result = mockMvc.perform(get("/api/v1/objective")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        /* Assert */
        assertThat(result).isEqualTo("[]");
    }

    @Test
    public void getObjective_when_then() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priority = Priority.builder().id(1L).level(1).description("Important").member(john).build();

        List<Objective> stub = List.of(
                Objective.builder().id(3L).member(john).priority(priority).build(),
                Objective.builder().id(2L).member(john).priority(priority).build(),
                Objective.builder().id(4L).member(john).priority(priority).build(),
                Objective.builder().id(1L).member(john).priority(priority).build()
        );

        when(objectiveServiceMock.findByObjective(any(Objective.class))).thenReturn(stub);

        /* Act */
        String result = mockMvc.perform(get("/api/v1/objective")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        /* Assert */
        List<Objective.Response> responses = Arrays.asList(objectMapper.readValue(result, Objective.Response[].class));
        log.info("YKD : " + responses);

        Objective.Response prev = null;
        for(Objective.Response response : responses) {
            if(Objects.isNull(prev)) {
                prev = response;
                continue;
            }

            assertThat(prev.getId()).isLessThan(response.getId());
            prev = response;
        }
    }

    @Test
    public void postObjective_whenPostParametersAreWell_thenReturnNormalResponse() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priority = Priority.builder().id(1L).level(1).description("Important").member(john).build();

        Objective parent = Objective.builder().id(1L).type(Cell.Type.Objective).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Prepared).title("title1").description("desc1").member(john).priority(priority).build();

        Objective.Post stub = Objective.Post.builder().startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).email(john.getEmail()).title("title from test").description("desc from test").priorityId(priority.getId()).parentId(parent.getId()).build();

        String content = objectMapper.writeValueAsString(stub);

        when(priorityServiceMock.findByIdAndMemberEmail(anyLong(), anyString())).thenReturn(Optional.of(priority));
        when(priorityServiceMock.findAnyOne()).thenReturn(Optional.of(priority));
        when(objectiveServiceMock.findById(anyLong())).thenReturn(Optional.of(parent));
        when(memberServiceMock.findByEmail(anyString())).thenReturn(Optional.of(john));
        when(objectiveServiceMock.save(any(Objective.class))).thenReturn(Optional.of(Objective.of(stub, priority, john, parent)));

        /* Act */
        String result = mockMvc.perform(post("/api/v1/objective")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        /* Assert */
        Objective.Response response = objectMapper.readValue(result, Objective.Response.class);
        assertThat(response.getEmail()).isEqualTo(stub.getEmail());
        assertThat(response.getTitle()).isEqualTo(stub.getTitle());
        assertThat(response.getDescription()).isEqualTo(stub.getDescription());
    }

    @Test
    public void putObjective_normalCase() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Priority priority = Priority.builder().id(1L).level(1).description("Important").member(john).build();

        Objective parent = Objective.builder().id(1L).type(Cell.Type.Objective).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Prepared).title("title1").description("desc1").member(john).priority(priority).build();

        Objective.Put stub = Objective.Put.builder().id(2L).startDate(LocalDate.now()).endDate(LocalDate.now())
                .status(Cell.Status.Complete).email(john.getEmail()).title("title from test").description("desc from test").priorityId(priority.getId()).parentId(parent.getId()).build();

        String content = objectMapper.writeValueAsString(stub);

        when(priorityServiceMock.findByIdAndMemberEmail(anyLong(), anyString())).thenReturn(Optional.of(priority));
        when(objectiveServiceMock.findById(anyLong())).thenReturn(Optional.of(parent));
        when(memberServiceMock.findByEmail(anyString())).thenReturn(Optional.of(john));
        when(objectiveServiceMock.save(any(Objective.class))).thenReturn(Optional.of(Objective.of(stub, priority, john, parent)));

        /* Act, Assert */
        mockMvc.perform(put("/api/v1/objective/" + stub.getId())
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
package com.kidongyun.bridge.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidongyun.bridge.api.config.SecurityConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.exception.Advice;
import com.kidongyun.bridge.api.security.TokenProvider;
import com.kidongyun.bridge.api.service.MemberService;
import com.kidongyun.bridge.api.service.ObjectiveService;
import com.kidongyun.bridge.api.service.PlanService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PlanController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PlanControllerTest {
    @MockBean
    private SecurityConfig securityConfigMock;
    @MockBean
    private TokenProvider tokenProviderMock;
    @MockBean
    private PlanService planServiceMock;
    @MockBean
    private ObjectiveService objectiveServiceMock;
    @MockBean
    private MemberService memberServiceMock;
    @InjectMocks
    private PlanController planControllerMock;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Advice advice;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(planControllerMock)
                .setControllerAdvice(advice)
                .build();
    }

    @Test
    public void getPlan_normalCase() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Objective obj = Objective.builder().id(1L).type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("title1").description("desc1").member(john).build();

        Plan stub = Plan.builder().id(2L).type(Cell.Type.Plan).startDate(LocalDate.now())
                .endDate(LocalDate.now()).member(john).content("test content").objective(obj).build();

        when(planServiceMock.findByType(Cell.Type.Plan)).thenReturn(Set.of(stub));

        /* Act, Assert */
        String response = mockMvc.perform(get("/api/v1/plan")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Plan> result = Arrays.asList(objectMapper.readValue(response, Plan[].class));

        /* Assert */
        assertThat(result.get(0).getId()).isEqualTo(stub.getId());
        assertThat(result.get(0).getContent()).isEqualTo(stub.getContent());
    }

    @Test
    public void getPlanByEmail_when_then() throws Exception {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();

        Objective objStub = Objective.builder().id(1L).type(Cell.Type.Objective).startDate(LocalDate.now())
                .endDate(LocalDate.now()).status(Cell.Status.Prepared).title("title1").description("desc1").member(john).build();

        Plan planStub1 = Plan.builder().id(2L).type(Cell.Type.Plan).startDate(LocalDate.now())
                .endDate(LocalDate.now()).member(john).content("test content1").objective(objStub).build();

        Plan planStub2 = Plan.builder().id(3L).type(Cell.Type.Plan).startDate(LocalDate.now())
                .endDate(LocalDate.now()).member(john).content("test content2").objective(objStub).build();

        when(planServiceMock.findByMemberEmail(anyString())).thenReturn(Set.of(planStub1, planStub2));

        /* Act */
        String response = mockMvc.perform(get("/api/v1/plan/email/" + john.getEmail())
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Plan.Response> results = Arrays.asList(objectMapper.readValue(response, Plan.Response[].class));

        /* Assert */
        assertThat(results.size()).isEqualTo(2);
        for(Plan.Response result : results) {
            assertThat(result.getEmail()).isEqualTo(john.getEmail());
            assertThat(result.getType()).isEqualTo(Cell.Type.Plan);
        }
    }

    @Test
    public void postPlan_normalCase() throws Exception {
        /* Arrange */

    }
}
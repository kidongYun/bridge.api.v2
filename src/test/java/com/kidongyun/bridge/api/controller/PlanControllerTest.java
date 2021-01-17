package com.kidongyun.bridge.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.exception.Advice;
import com.kidongyun.bridge.api.repository.plan.PlanRepository;
import com.kidongyun.bridge.api.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class PlanControllerTest {
    @Mock
    private PlanService planServiceMock;
    @InjectMocks
    private PlanController planControllerMock;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Advice advice;

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

        Objective obj = Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).status(Cell.Status.Prepared).title("title1").description("desc1").member(john).build();

        Plan stub = Plan.builder().id(2L).type(Cell.Type.Plan).startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now()).member(john).content("test content").objective(obj).build();

        when(planServiceMock.findByType(Cell.Type.Plan)).thenReturn(Set.of(stub));

        /* Act, Assert */
        String response = mockMvc.perform(get("/api/v1/plan")
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Plan> result = Arrays.asList(objectMapper.readValue(response, Plan[].class));

        /* Assert */
        assertThat(result.get(0).getId()).isEqualTo(stub.getId());
        assertThat(result.get(0).getContent()).isEqualTo(stub.getContent());
    }

    @Test
    public void postPlan_normalCase() throws Exception {
        /* Arrange */

    }
}
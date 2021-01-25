package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.config.SecurityConfig;
import com.kidongyun.bridge.api.exception.Advice;
import com.kidongyun.bridge.api.security.TokenProvider;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PriorityController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PriorityControllerTest {
    @MockBean
    private SecurityConfig securityConfigMock;
    @MockBean
    private TokenProvider tokenProviderMock;
    @MockBean
    private PriorityService priorityServiceMock;
    @InjectMocks
    private PriorityController priorityControllerMock;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    Advice advice;
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(priorityControllerMock)
                .setControllerAdvice(advice)
                .build();
    }

    @Test
    public void test_when_then() {

    }
}
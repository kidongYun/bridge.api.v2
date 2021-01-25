package com.kidongyun.bridge.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidongyun.bridge.api.config.SecurityConfig;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.exception.Advice;
import com.kidongyun.bridge.api.security.TokenProvider;
import com.kidongyun.bridge.api.service.MemberService;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SignController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class SignControllerTest {
    @MockBean
    private SecurityConfig securityConfigMock;
    @MockBean
    private TokenProvider tokenProviderMock;
    @MockBean
    private MemberService memberServiceMock;
    @InjectMocks
    private SignController signControllerMock;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(signControllerMock)
                .setControllerAdvice(new Advice()).build();
    }

    @Test
    public void signUp_whenEmailIsEmpty_thenReturn400() throws Exception {
        /* Arrange */
        Member.SignUp stub = Member.SignUp.builder().email("").password("123123").build();
        String content = objectMapper.writeValueAsString(stub);

        /* Act, Assert */
        String response = mockMvc.perform(post("/api/v1/sign/up")
                .characterEncoding("utf-8")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        /* Assert */
        assertThat(response).isEqualTo("'email' must not be empty");
    }

    @Test
    public void signIn_whenEmailDoesNotExist_thenReturn400() throws Exception {
        /* Arrange */
        String content = objectMapper.writeValueAsString(Member.SignIn.builder().email("").build());
        when(memberServiceMock.isNotExist(anyString())).thenReturn(true);

        /* Act, Assert  */
        String response = mockMvc.perform(post("/api/v1/sign/in")
                .characterEncoding("utf-8")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        /* Assert */
        assertThat(response).isEqualTo("'email' is not existed");
    }

    @Test
    public void signIn_whenPasswordDoesNotMatch_thenReturn400() throws Exception {
        /* Arrange */
        Member.SignIn in = Member.SignIn.builder().email("test").password("1").build();
        String content = objectMapper.writeValueAsString(in);
        when(memberServiceMock.isNotExist(anyString())).thenReturn(false);
        when(memberServiceMock.findByEmail(anyString())).thenReturn(Optional.of(Member.builder().email("test").password("2").build()));

        /* Act, Assert  */
        String response = mockMvc.perform(post("/api/v1/sign/in")
                .characterEncoding("utf-8")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        /* Assert */
        assertThat(response).isEqualTo("'password' is not matched");
    }
}
package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepositoryMock;
    @InjectMocks
    MemberService memberServiceMock;
    @Autowired
    MemberService memberService;

    @Test(expected = HttpClientErrorException.class)
    public void isExist_emailIsNull() {
        /* Arrange, Act, Assert */
        memberService.isExist(null);
    }

    @Test
    public void isExist_normalCase() {
        /* Arrange */
        when(memberRepositoryMock.existsById(anyString())).thenReturn(true);

        /* Act */
        boolean result = memberServiceMock.isExist(anyString());

        /* Assert */
        assertThat(result).isEqualTo(true);
    }

    @Test(expected = HttpClientErrorException.class)
    public void save_memberIsNull() {
        /* Arrange, Act, Assert */
        memberService.save(null);
    }

    @Test
    public void save_normalCase() {
        /* Arrange */
        Member john = Member.builder().email("john@gmail.com").password("q1w2e3r4").build();
        when(memberRepositoryMock.save(any(Member.class))).thenReturn(john);

        /* Act */
        Member result = memberServiceMock.save(john);

        /* Assert */
        assertThat(result.getEmail()).isEqualTo(john.getEmail());
        assertThat(result.getPassword()).isEqualTo(john.getPassword());
    }
}
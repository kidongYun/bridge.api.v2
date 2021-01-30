package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.entity.Plan;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class PlanServiceTest {
    @Autowired
    PlanService planService;

    @Test
    public void findByObjectiveId_whenIdIsNull_thenResultIsEmpty() {
        /* Arrange, Act */
        Set<Plan> result = planService.findByObjectiveId(null);

        /* Assert */
        assertThat(result.isEmpty()).isTrue();
    }
}
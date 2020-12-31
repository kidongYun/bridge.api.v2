package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjectiveServiceTest {
    @Autowired
    ObjectiveService objectiveService;

    @Test
    public void traversal_normal() {
        /* Arrange */
        Objective root = Objective.builder().id(1L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").title("title1").description("desc1").build();

        root.getChildren().add(Objective.builder().id(2L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title2").description("desc2").parent(Objective.builder().id(1L).build()).build());

        root.getChildren().add(Objective.builder().id(3L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title3").description("desc3").parent(Objective.builder().id(1L).build()).build());

        root.getChildren().stream().filter(child -> child.getId() == 2L).forEach(child ->
                child.getChildren().add(Objective.builder().id(4L).type(Cell.Type.Objective).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("prepared").title("title4").description("desc4").parent(Objective.builder().id(2L).build()).build()));

        /* Act */
        objectiveService.traversal(root);
    }
}
package com.kidongyun.bridge.api;

import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.vo.TreeNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class TestRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TreeNode<Objective> root = new TreeNode.Builder<Objective>().data(Objective.builder().id(1L).build()).build();

        root.addChild(Objective.builder().id(2L).build());
        root.addChild(Objective.builder().id(3L).build());
        root.getChildren().get(0).addChild(Objective.builder().id(4L).build());

        List<Objective> objectiveList = root.traversal();

        for(Objective objective : objectiveList) {
            log.info(objective.getId() + " ");
        }
    }
}

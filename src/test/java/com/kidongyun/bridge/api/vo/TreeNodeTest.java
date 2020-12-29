package com.kidongyun.bridge.api.vo;

import com.kidongyun.bridge.api.entity.Objective;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class TreeNodeTest {

    @Test
    public void addChild_normal() {
        /* Arrange */
        TreeNode<Objective> root = new TreeNode.Builder<Objective>().data(Objective.builder().id(1L).build()).build();
        root.addChild(Objective.builder().id(2L).build());
        root.addChild(Objective.builder().id(3L).build());

        /* Assert */
        assertThat(root.getChildren().size()).isEqualTo(2);
        assertThat(root.getChildren().get(0).getData().getId()).isEqualTo(2L);
        assertThat(root.getChildren().get(1).getData().getId()).isEqualTo(3L);
    }

    @Test
    public void removeChild_byId() {
        /* Arrange */
        TreeNode<Objective> root = new TreeNode.Builder<Objective>().data(Objective.builder().id(1L).build()).build();
        root.addChild(Objective.builder().id(2L).build());
        root.addChild(Objective.builder().id(3L).build());
        root.addChild(Objective.builder().id(4L).build());
        assertThat(root.getChildren().size()).isEqualTo(3);

        /* Act */
        root.removeChild(obj -> obj.getId() == 3L);

        /* Assert */
        assertThat(root.getChildren().size()).isEqualTo(2);
        assertThat(root.getChildren().get(0).getData().getId()).isEqualTo(2L);
        assertThat(root.getChildren().get(1).getData().getId()).isEqualTo(4L);
    }

    @Test
    public void removeChild_byPriority() {
        /* Arrange */
        TreeNode<Objective> root = new TreeNode.Builder<Objective>().data(Objective.builder().id(1L).build()).build();
        root.addChild(Objective.builder().id(2L).priority(1).build());
        root.addChild(Objective.builder().id(3L).priority(1).build());
        root.addChild(Objective.builder().id(4L).priority(2).build());
        assertThat(root.getChildren().size()).isEqualTo(3);

        /* Act */
        root.removeChild(obj -> obj.getPriority() == 1);

        /* Assert */
        assertThat(root.getChildren().size()).isEqualTo(1);
        assertThat(root.getChildren().get(0).getData().getId()).isEqualTo(4L);
    }

    @Test
    public void traversal_normal() {
        /* Arrange */
        TreeNode<Objective> root = new TreeNode.Builder<Objective>().data(Objective.builder().id(1L).build()).build();
        root.addChild(Objective.builder().id(2L).build());
        root.addChild(Objective.builder().id(3L).build());
        root.getChildren().get(0).addChild(Objective.builder().id(4L).build());

        /* Act */
        List<Objective> results = root.traversal();

        /* Assert */
        assertThat(results.size()).isEqualTo(4);
        assertThat(results.get(0).getId()).isEqualTo(1);
        assertThat(results.get(1).getId()).isEqualTo(2);
        assertThat(results.get(2).getId()).isEqualTo(4);
        assertThat(results.get(3).getId()).isEqualTo(3);
    }
}
package com.kidongyun.bridge.api.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Slf4j
@Getter
@Setter
public class TreeNode<T> {
    private T data;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode() {
        children = new ArrayList<>();
    }

    public List<T> traversal() {
        return dfs(this, new ArrayList<>());
    }

    public void addChild(T data) {
        children.add(new TreeNode.Builder<T>().data(data).parent(this).build());
    }

    public void removeChild(Predicate<T> condition) {
        List<TreeNode<T>> result = new ArrayList<>();
        for(TreeNode<T> child : children) {
            if(condition.test(child.data)) {
                continue;
            }
            result.add(child);
        }
        this.setChildren(result);
    }

    private List<T> dfs(TreeNode<T> treeNode, List<T> dataList) {
        dataList.add(treeNode.data);

        for(TreeNode<T> child : treeNode.children) {
            child.dfs(child, dataList);
        }

        return dataList;
    }

    public TreeNode<T> of(Set<T> set) {
        return new TreeNode.Builder<T>().build();
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public static class Builder<T> {
        private final TreeNode<T> treeNode;

        public Builder() {
            this.treeNode = new TreeNode<>();
        }

        public Builder<T> data(T val) {
            this.treeNode.data = val;
            return this;
        }

        public Builder<T> parent(TreeNode<T> val) {
            this.treeNode.parent = val;
            return this;
        }

        public Builder<T> children(List<TreeNode<T>> val) {
            this.treeNode.children = val;
            return this;
        }

        public TreeNode<T> build() {
            return this.treeNode;
        }
    }
}

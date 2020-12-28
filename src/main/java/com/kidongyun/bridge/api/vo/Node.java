package com.kidongyun.bridge.api.vo;

import java.util.List;

public class Node<T> {
    private T data;
    private Node<T> parent;
    private List<Node<T>> children;
}

package com.kidongyun.bridge.api.security;

public interface AuthToken<T> {
    boolean validate();
    T getData();
}

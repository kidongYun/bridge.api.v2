package com.kidongyun.bridge.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Getter
@Setter
@ToString(exclude = "cells")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member implements UserDetails {
    @Id
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<Cell> cells = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<Priority> priorities = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        @NotBlank(message = "'email' 항목은 필수 값입니다")
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @NotBlank(message = "'password' 항목은 필수 값입니다")
        @ApiModelProperty(example = "q1w2e3r4")
        private String password;
        @ApiModelProperty(hidden = true)
        private List<String> roles;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignIn {
        @NotBlank(message = "'email' 항목은 필수 값입니다")
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @NotBlank(message = "'password' 항목은 필수 값입니다")
        @ApiModelProperty(example = "john123123")
        private String password;
    }

    public static Member of(SignUp up) {
        return Member.builder().email(up.getEmail()).password(up.getPassword()).roles(up.getRoles()).build();
    }

    public static Member of(SignIn in) {
        return Member.builder().email(in.getEmail()).password(in.getPassword()).build();
    }
}

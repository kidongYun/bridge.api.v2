package com.kidongyun.bridge.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    private String auth;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<Cell> cells = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<Priority> priorities = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for(String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }

        return roles;
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
    public static class Post {
        @NotNull
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "john123123")
        private String password;
        @ApiModelProperty(hidden = true)
        private String auth;
    }

    public static Member of(Post post) {
        return Member.builder().email(post.getEmail()).password(post.getPassword()).auth(post.getAuth()).build();
    }
}

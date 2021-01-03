package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Member {
    @Id
    private String email;

    private String password;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<Cell> cells = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private Set<Priority> priorities = new HashSet<>();
}

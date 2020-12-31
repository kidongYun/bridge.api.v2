package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Objective extends Cell {
    private String title;

    private String description;

    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Objective parent;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Objective> children = new HashSet<>();
}

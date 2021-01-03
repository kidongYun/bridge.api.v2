package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Slf4j
@Getter
@Setter
@ToString(exclude = "objective")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plan extends Cell {
    private String content;

    @ManyToOne
    private Objective objective;
}

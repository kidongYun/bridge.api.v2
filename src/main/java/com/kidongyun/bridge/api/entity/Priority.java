package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Priority {
    @Id
    @GeneratedValue
    private Long id;

    private Integer level;

    private String description;
}

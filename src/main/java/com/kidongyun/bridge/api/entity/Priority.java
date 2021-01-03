package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Slf4j
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Priority {
    @Id
    private Integer level;

    private String description;

    @OneToMany(mappedBy = "priority")
    private Set<Objective> objectives;
}

package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Slf4j
@Getter
@Setter
@ToString(exclude = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Priority {
    @Id
    private Integer level;

    private String description;

    @ManyToOne
    private Member member;

//    @OneToMany(mappedBy = "priority")
//    private Set<Objective> objectives;
}

package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    @GeneratedValue
    private Long id;

    private Integer level;

    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "priority")
    private Set<Objective> objectives = new HashSet<>();
}

package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@ToString(exclude = "member")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Cell {
    public enum Type { Objective, Plan, Todo }
    public enum Status { Complete, Progress, Prepared }

    @Id
    @GeneratedValue
    protected Long id;

    protected LocalDateTime startDateTime;

    protected LocalDateTime endDateTime;

    protected Status status;

    protected Type type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    protected Member member;
}

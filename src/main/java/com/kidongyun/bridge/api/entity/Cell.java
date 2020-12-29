package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Cell {
    public enum Type { Objective, Plan, Todo }

    @Id
    @GeneratedValue
    protected Long id;

    protected LocalDateTime startDateTime;

    protected LocalDateTime endDateTime;

    protected String status;

    protected Type type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    protected Member member;
}

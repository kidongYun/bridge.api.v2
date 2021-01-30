package com.kidongyun.bridge.api.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    protected LocalDate startDate;
    protected LocalTime startTime;

    protected LocalDate endDate;
    protected LocalTime endTime;

    @Enumerated(EnumType.STRING)
    protected Status status;

    protected Type type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    protected Member member;

    public static Cell empty() {
        return new Cell();
    }
}

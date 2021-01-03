package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Objective extends Cell {
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Priority priority;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "objective")
    private Set<Plan> plans;

    @ManyToOne(fetch = FetchType.LAZY)
    private Objective parent;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Objective> children = new HashSet<>();

    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String status;
        private Type type;
        private String email;
        private String title;
        private String description;
        private Priority priority;
        private Long parentId;
        private Set<Long> childrenId;

        public static Response of(Objective obj) {
            return Response.builder().id(obj.getId()).startDateTime(obj.getStartDateTime()).endDateTime(obj.getEndDateTime()).status(obj.status)
                    .type(obj.type).email(Objects.requireNonNullElse(obj.member, Member.builder().build()).getEmail())
                    .title(obj.getTitle()).description(obj.getDescription()).priority(obj.getPriority())
                    .parentId(Objects.requireNonNullElse(obj.getParent(), Objective.builder().build()).getId())
                    .childrenId(obj.getChildren().stream().map(Cell::getId).collect(toSet())).build();
        }
    }
}

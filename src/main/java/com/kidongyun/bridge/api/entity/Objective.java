package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ToString(exclude = {"priority", "plans", "parent", "children"}, callSuper = true)
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
    @ToString
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

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private LocalDateTime endDateTime;
        private String status;
        private String email;
        private String title;
        private String description;
        private Integer priorityLevel;
        private Long parentId;

        public Objective toDomain() {
            return Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(endDateTime).status(status)
                    .type(Type.Objective).member(Member.builder().email(email).build()).title(title).description(description)
                    .priority(Priority.builder().level(priorityLevel).build()).parent(Objective.builder().id(parentId).build()).build();
        }
    }
}

package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Priority priority;

    @Builder.Default
    @OneToMany(mappedBy = "objective")
    private Set<Plan> plans = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Objective parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
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
        private Cell.Status status;
        private Type type;
        private String email;
        private String title;
        private String description;
        private Long priorityId;
        private Long parentId;
        private Set<Long> childrenId;

        public static Response of(Objective obj) {
            return Response.builder()
                    .id(obj.getId())
                    .startDateTime(obj.getStartDateTime())
                    .endDateTime(obj.getEndDateTime())
                    .status(obj.status)
                    .type(obj.type)
                    .email(Objects.requireNonNull(obj.getMember()).getEmail())
                    .title(obj.getTitle())
                    .description(obj.getDescription())
                    .priorityId(Objects.requireNonNull(obj.getPriority().getId()))
                    .parentId(Objects.requireNonNullElse(obj.getParent(), Objective.builder().build()).getId())
                    .childrenId(obj.getChildren().stream().map(Cell::getId).collect(toSet()))
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Cell.Status status;
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "post default title by swagger")
        private String title;
        @ApiModelProperty(example = "post default description by swagger")
        private String description;
        @ApiModelProperty(example = "1")
        private long priorityId;
        private long parentId;

        public Objective toDomain(Priority priority, Member member, Objective parent) {
            return Objective.builder()
                    .startDateTime(Objects.requireNonNullElse(startDateTime, LocalDateTime.now()))
                    .endDateTime(endDateTime)
                    .status(status)
                    .type(Type.Objective)
                    .member(member)
                    .title(title)
                    .description(description)
                    .priority(priority)
                    .parent(parent)
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Put {
        @ApiModelProperty(example = "1")
        private Long id;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Cell.Status status;
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "put default title by swagger")
        private String title;
        @ApiModelProperty(example = "put default description by swagger")
        private String description;
        @ApiModelProperty(example = "1")
        private long priorityId;
        private long parentId;

        public Objective toDomain(Priority priority, Member member, Objective parent) {
            return Objective.builder()
                    .id(id)
                    .startDateTime(Objects.requireNonNullElse(startDateTime, LocalDateTime.now()))
                    .endDateTime(endDateTime)
                    .status(status)
                    .type(Type.Objective)
                    .member(member)
                    .title(title)
                    .description(description)
                    .priority(priority)
                    .parent(parent)
                    .build();
        }
    }
}

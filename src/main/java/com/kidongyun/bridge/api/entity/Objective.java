package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private Status status;
        private Type type;
        private String email;
        private String title;
        private String description;
        private Long priorityId;
        private Long parentId;
        private Set<Long> childrenId;

        public static Response of(Objective obj) {
            Assert.notNull(obj, "parameter 'obj' must not be null");
            Assert.notNull(obj.getMember(), "parameter 'obj.member' must not be null");
            Assert.notNull(obj.getPriority(), "parameter 'obj.priority' must not be null");

            return Response.builder()
                    .id(obj.getId())
                    .startDate(obj.getStartDate())
                    .startTime(obj.getStartTime())
                    .endDate(obj.getEndDate())
                    .endTime(obj.getEndTime())
                    .status(obj.status)
                    .type(obj.type)
                    .email(obj.getMember().getEmail())
                    .title(obj.getTitle())
                    .description(obj.getDescription())
                    .priorityId(obj.getPriority().getId())
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
    public static class Get {
        private Long id;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        private Status status;
        private String email;
        private String title;
        private String description;
        private Long priorityId;
        private Long parentId;

        public static Get empty() {
            return Get.builder().build();
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private LocalDate startDate;
        private LocalDate endDate;
        private Status status;
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "post default title by swagger")
        private String title;
        @ApiModelProperty(example = "post default description by swagger")
        private String description;
        @ApiModelProperty(example = "1")
        private Long priorityId;
        private Long parentId;
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
        private LocalDate startDate;
        private LocalDate endDate;
        private Status status;
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "put default title by swagger")
        private String title;
        @ApiModelProperty(example = "put default description by swagger")
        private String description;
        @ApiModelProperty(example = "1")
        private Long priorityId;
        private Long parentId;
    }

    public static Objective of(Get get, Priority priority, Member member, Objective parent) {
        if(Objects.isNull(get)) {
            return Objective.empty();
        }

        return Objective.builder()
                .id(get.getId())
                .startDate(get.getStartDate())
                .endDate(get.getEndDate())
                .status(get.getStatus())
                .type(Type.Objective)
                .member(member)
                .title(get.getTitle())
                .description(get.getDescription())
                .priority(priority)
                .parent(parent)
                .build();
    }

    public static Objective of(Post post, Priority priority, Member member, Objective parent) {
        if(Objects.isNull(post)) {
            return Objective.empty();
        }

        return Objective.builder()
                .startDate(Objects.requireNonNullElse(post.getStartDate(), LocalDate.now()))
                .endDate(post.getEndDate())
                .status(Objects.requireNonNullElse(post.getStatus(), Status.Prepared))
                .type(Type.Objective)
                .member(member)
                .title(post.getTitle())
                .description(post.getDescription())
                .priority(priority)
                .parent(parent)
                .build();
    }

    public static Objective of(Put put, Priority priority, Member member, Objective parent) {
        if(Objects.isNull(put)) {
            return Objective.empty();
        }

        return Objective.builder()
                .id(put.getId())
                .startDate(Objects.requireNonNullElse(put.getStartDate(), LocalDate.now()))
                .endDate(put.getEndDate())
                .status(put.getStatus())
                .type(Type.Objective)
                .member(member)
                .title(put.getTitle())
                .description(put.getDescription())
                .priority(priority)
                .parent(parent)
                .build();
    }

    public static Objective empty() {
        return Objective.builder().build();
    }
}

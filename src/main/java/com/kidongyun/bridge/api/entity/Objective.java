package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

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
        private Status status;
        private Type type;
        private String email;
        private String title;
        private String description;
        private Long priorityId;
        private Long parentId;
        private Set<Long> childrenId;

        public static Response of(Objective obj) {
            if(Objects.isNull(obj) || Objects.isNull(obj.getMember()) || Objects.isNull(obj.getPriority())) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'obj', 'obj.member', 'obj.priority' must not be null");
            }

            return Response.builder()
                    .id(obj.getId())
                    .startDateTime(obj.getStartDateTime())
                    .endDateTime(obj.getEndDateTime())
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
        private Status status;
        private String email;
        private Long priorityId;
        private Long parentId;
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
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
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

    public static Objective of(Get get, Priority priority, Member member) {
        return Objective.builder()
                .id(get.getId())
                .build();
    }

    public static Objective of(Post post, Priority priority, Member member, Objective parent) {
        return Objective.builder()
                .startDateTime(Objects.requireNonNullElse(post.getStartDateTime(), LocalDateTime.now()))
                .endDateTime(post.getEndDateTime())
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
        return Objective.builder()
                .id(put.getId())
                .startDateTime(Objects.requireNonNullElse(put.getStartDateTime(), LocalDateTime.now()))
                .endDateTime(put.getEndDateTime())
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

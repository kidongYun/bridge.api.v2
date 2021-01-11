package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@ToString(exclude = "objective", callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plan extends Cell {
    private String content;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Objective objective;

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
        private String content;
        private Long objectiveId;

        public static Response of(Plan plan) {
            if(Objects.isNull(plan)) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'plan' parameter should not be null");
            }

            if(Objects.isNull(plan.getMember())) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'plan.member' should not be null");
            }

            if(Objects.isNull(plan.getObjective())) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'plan.objective' should not be null");
            }

            return Response.builder()
                    .id(plan.getId())
                    .startDateTime(plan.getStartDateTime())
                    .endDateTime(plan.getEndDateTime())
                    .status(plan.getStatus())
                    .type(plan.getType())
                    .email(plan.getMember().getEmail())
                    .content(plan.getContent())
                    .objectiveId(plan.getObjective().getId())
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    public static class Post {
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Cell.Status status;
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "post default content by swagger")
        private String content;
        @ApiModelProperty(example = "9")
        private Long objectiveId;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    public static class Put {
        @ApiModelProperty(example = "15")
        private Long id;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private Cell.Status status;
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "post default content by swagger")
        private String content;
        @ApiModelProperty(example = "9")
        private Long objectiveId;
    }

    public static Plan of(Post post, Member member, Objective parent) {
        if(Objects.isNull(member) || Objects.isNull(parent)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'member', 'parent' parameters should be not null");
        }

        return Plan.builder()
                .startDateTime(post.getStartDateTime())
                .endDateTime(post.getEndDateTime())
                .status(post.getStatus())
                .member(member)
                .content(post.getContent())
                .objective(parent)
                .build();
    }

    public static Plan of(Put put, Member member, Objective parent) {
        if(Objects.isNull(member) || Objects.isNull(parent)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'member', 'parent' parameters should be not null");
        }

        return Plan.builder()
                .id(put.getId())
                .startDateTime(put.getStartDateTime())
                .endDateTime(put.getEndDateTime())
                .status(put.getStatus())
                .member(member)
                .content(put.getContent())
                .objective(parent)
                .build();
    }
}

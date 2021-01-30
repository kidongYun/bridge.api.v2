package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private Cell.Status status;
        private Type type;
        private String email;
        private String content;
        private Long objectiveId;

        public static Response of(Plan plan) {
            Assert.notNull(plan, "'plan' parameter must not be null");
            Assert.notNull(plan.getMember(), "'plan.member' must not be null");
            Assert.notNull(plan.getObjective(), "'plan.objective' must not be null");

            return Response.builder()
                    .id(plan.getId())
                    .startDate(plan.getStartDate())
                    .startTime(plan.getStartTime())
                    .endDate(plan.getEndDate())
                    .endTime(plan.getEndTime())
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private LocalDate startDate;
        private LocalDate endDate;
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Put {
        @ApiModelProperty(example = "15")
        private Long id;
        private LocalDate startDate;
        private LocalDate endDate;
        private Cell.Status status;
        @ApiModelProperty(example = "john@gmail.com")
        private String email;
        @ApiModelProperty(example = "post default content by swagger")
        private String content;
        @ApiModelProperty(example = "9")
        private Long objectiveId;
    }

    public static Plan of(Post post, Member member, Objective objective) {
        Assert.notNull(post, "파라미터 'Post' 객체는 필수 값입니다");
        Assert.notNull(member, "파라미터 'Member' 객체는 필수 값입니다");
        Assert.notNull(objective, "파라미터 'Objective' 객체는 필수 값입니다");

        return Plan.builder()
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .status(post.getStatus())
                .member(member)
                .content(post.getContent())
                .objective(objective)
                .build();
    }

    public static Plan of(Put put, Member member, Objective objective) {
        Assert.notNull(put, "파라미터 'Put' 객체는 필수 값입니다");
        Assert.notNull(member, "파라미터 'Member' 객체는 필수 값입니다");
        Assert.notNull(objective, "파라미터 'Objective' 객체는 필수 값입니다");

        return Plan.builder()
                .id(put.getId())
                .startDate(put.getStartDate())
                .endDate(put.getEndDate())
                .status(put.getStatus())
                .member(member)
                .content(put.getContent())
                .objective(objective)
                .build();
    }
}

package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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
}

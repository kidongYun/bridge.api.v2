package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

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

    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    private Objective parent;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Objective> children = new HashSet<>();

    @JsonIgnore
    public Set<Objective> getChildren() {
        return children;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private Integer priority;
        private Objective parent;
        private Set<Objective> children;
    }
}

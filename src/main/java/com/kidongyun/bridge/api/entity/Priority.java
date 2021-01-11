package com.kidongyun.bridge.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Getter
@Setter
@ToString(exclude = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Priority {
    @Id
    @GeneratedValue
    private Long id;

    private Integer level;

    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "priority")
    private Set<Objective> objectives = new HashSet<>();

    @Getter
    @Setter
    @ToString
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private Long id;
        private Integer level;
        private String description;
        private String email;
        private Set<Long> objectiveIds;

        public static Response of(Priority priority) {
            return Response.builder()
                    .id(priority.getId())
                    .level(priority.getLevel())
                    .description(priority.getDescription())
                    .email(priority.getMember().getEmail())
                    .objectiveIds(priority.getObjectives().stream().map(Cell::getId).collect(toSet()))
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    @Builder
    public static class Post {
        private Integer level;
        private String description;
        private String email;

        public static Priority toDomain(Post post, Member member) {
            return Priority.builder()
                    .level(post.getLevel())
                    .description(post.getDescription())
                    .member(member)
                    .build();
        }
    }
}

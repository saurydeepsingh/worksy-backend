package com.worksy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "practice_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "dsa_problem_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PracticeProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dsa_problem_id", nullable = false)
    private DsaProblem problem;

    @Builder.Default
    private boolean completed = false;

    private LocalDateTime completedAt;
}

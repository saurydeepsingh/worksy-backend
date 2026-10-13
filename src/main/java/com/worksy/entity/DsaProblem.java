package com.worksy.entity;

import com.worksy.entity.enums.Difficulty;
import com.worksy.entity.enums.DsaCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dsa_problems")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DsaProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DsaCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Difficulty difficulty;

    @Column(columnDefinition = "TEXT")
    private String example;

    @Column(columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(columnDefinition = "TEXT")
    private String solutionExplanation;
}

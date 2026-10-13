package com.worksy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "practice_sheets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PracticeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer totalItems;
}

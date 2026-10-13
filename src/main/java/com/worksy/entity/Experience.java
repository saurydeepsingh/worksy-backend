package com.worksy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "experience")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, length = 150)
    private String jobTitle;

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder.Default
    private boolean currentlyWorking = false;

    @Column(columnDefinition = "TEXT")
    private String description;
}

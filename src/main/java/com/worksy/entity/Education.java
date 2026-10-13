package com.worksy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "education")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String institution;

    @Column(nullable = false, length = 150)
    private String degree;

    @Column(length = 100)
    private String fieldOfStudy;

    private Integer startYear;
    private Integer endYear;

    private Double grade;
}

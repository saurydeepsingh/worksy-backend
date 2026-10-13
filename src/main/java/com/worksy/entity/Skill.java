package com.worksy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;
}

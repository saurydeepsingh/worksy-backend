package com.worksy.entity;

import com.worksy.entity.enums.JobType;
import com.worksy.entity.enums.WorkMode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by_hr_id", nullable = false)
    private HRProfile postedBy;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "job_required_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @Builder.Default
    private Set<Skill> requiredSkills = new HashSet<>();

    private Integer requiredExperience;

    private Double minSalary;
    private Double maxSalary;

    @Column(length = 150)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkMode workMode;

    @Builder.Default
    private Integer numberOfOpenings = 1;

    @Column(length = 150)
    private String qualification;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    private LocalDate applicationDeadline;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private boolean removedByAdmin = false;

    private LocalDateTime postedDate;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        postedDate = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

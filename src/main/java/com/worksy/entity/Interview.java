package com.worksy.entity;

import com.worksy.entity.enums.InterviewStatus;
import com.worksy.entity.enums.InterviewType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private JobApplication application;

    @Column(nullable = false)
    private LocalDateTime interviewDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InterviewType interviewType;

    private String meetingLink;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

package com.worksy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "practice_sheet_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sheet_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PracticeSheetProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sheet_id", nullable = false)
    private PracticeSheet sheet;

    @Builder.Default
    private Integer itemsCompleted = 0;
}

package com.worksy.repository;

import com.worksy.entity.PracticeSheetProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PracticeSheetProgressRepository extends JpaRepository<PracticeSheetProgress, Long> {
    List<PracticeSheetProgress> findByUserId(Long userId);
    Optional<PracticeSheetProgress> findByUserIdAndSheetId(Long userId, Long sheetId);
}

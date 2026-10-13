package com.worksy.repository;

import com.worksy.entity.PracticeProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PracticeProgressRepository extends JpaRepository<PracticeProgress, Long> {
    List<PracticeProgress> findByUserId(Long userId);
    Optional<PracticeProgress> findByUserIdAndProblemId(Long userId, Long problemId);
    long countByUserIdAndCompletedTrue(Long userId);
}

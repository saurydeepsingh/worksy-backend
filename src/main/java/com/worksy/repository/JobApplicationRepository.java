package com.worksy.repository;

import com.worksy.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobIdAndUserId(Long jobId, Long userId);
    Optional<JobApplication> findByJobIdAndUserId(Long jobId, Long userId);
    Page<JobApplication> findByUserId(Long userId, Pageable pageable);
    Page<JobApplication> findByJobId(Long jobId, Pageable pageable);
    List<JobApplication> findByJobIdIn(List<Long> jobIds);
    long countByJobId(Long jobId);
}

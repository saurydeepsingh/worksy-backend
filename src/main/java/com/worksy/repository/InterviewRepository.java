package com.worksy.repository;

import com.worksy.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Optional<Interview> findByApplicationId(Long applicationId);
    List<Interview> findByApplication_UserId(Long userId);
}

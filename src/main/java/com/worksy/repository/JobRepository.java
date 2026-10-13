package com.worksy.repository;

import com.worksy.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    Page<Job> findByPostedByIdAndActiveTrue(Long hrId, Pageable pageable);
    Page<Job> findByPostedById(Long hrId, Pageable pageable);
    Page<Job> findByActiveTrueAndRemovedByAdminFalse(Pageable pageable);
}

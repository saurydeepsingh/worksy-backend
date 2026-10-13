package com.worksy.repository;

import com.worksy.entity.DsaProblem;
import com.worksy.entity.enums.DsaCategory;
import com.worksy.entity.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DsaProblemRepository extends JpaRepository<DsaProblem, Long> {
    Page<DsaProblem> findByCategory(DsaCategory category, Pageable pageable);
    Page<DsaProblem> findByDifficulty(Difficulty difficulty, Pageable pageable);
}

package com.worksy.repository;

import com.worksy.entity.HRProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HRProfileRepository extends JpaRepository<HRProfile, Long> {
    Optional<HRProfile> findByEmail(String email);
    boolean existsByEmail(String email);
}

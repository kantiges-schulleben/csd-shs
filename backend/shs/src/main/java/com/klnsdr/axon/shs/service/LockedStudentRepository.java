package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.LockedEnrolledStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockedStudentRepository extends JpaRepository<LockedEnrolledStudentEntity, Long> {
}

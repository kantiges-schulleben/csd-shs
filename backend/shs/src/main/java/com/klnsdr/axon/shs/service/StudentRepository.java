package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.EnrolledStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<EnrolledStudentEntity, Long> {
}

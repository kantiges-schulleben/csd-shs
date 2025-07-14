package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.EnrolledStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<EnrolledStudentEntity, Long> {
    List<EnrolledStudentEntity> findByNameContainingIgnoreCase(String name);
}

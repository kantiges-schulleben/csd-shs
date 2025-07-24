package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.LockedEnrolledStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LockedStudentRepository extends JpaRepository<LockedEnrolledStudentEntity, Long> {
    @Query("""
       SELECT s FROM LockedEnrolledStudentEntity s
       WHERE s.id NOT IN (
           SELECT g.teacher.id FROM Group g WHERE g.teacher IS NOT NULL
       )
       AND s.id NOT IN (
           SELECT stu.id FROM Group g JOIN g.students stu
       )
       """)
    List<LockedEnrolledStudentEntity> getWithoutPartner();

    List<LockedEnrolledStudentEntity> findBySubjectAndIsTeacherIsFalse(String subject);
    List<LockedEnrolledStudentEntity> findBySubjectAndIsTeacherIsTrue(String subject);
}

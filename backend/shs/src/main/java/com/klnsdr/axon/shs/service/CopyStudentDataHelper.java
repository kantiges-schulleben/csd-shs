package com.klnsdr.axon.shs.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class CopyStudentDataHelper {
    private final EntityManager entityManager;

    public CopyStudentDataHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void copyStudentsTable() {
        entityManager.createNativeQuery(
                "INSERT INTO locked_enrolled_students SELECT * from enrolled_students"
        ).executeUpdate();
    }

    @Transactional
    public void clearLockedStudentsTable() {
        entityManager.createNativeQuery(
                "DELETE FROM locked_enrolled_students"
        ).executeUpdate();
    }
}

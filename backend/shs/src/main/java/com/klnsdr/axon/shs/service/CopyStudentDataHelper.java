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
    public boolean copyStudentsTable() {
        return entityManager.createNativeQuery(
                "INSERT INTO locked_enrolled_students SELECT * from enrolled_students"
        ).executeUpdate() > 0;
    }

    @Transactional
    public boolean clearLockedStudentsTable() {
        return entityManager.createNativeQuery(
                "DELETE FROM locked_enrolled_students"
        ).executeUpdate() > 0;
    }
}

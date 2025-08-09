package com.klnsdr.axon.shs.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class CopyStudentDataHelperTest {
    private EntityManager entityManager;
    private Query query;
    private CopyStudentDataHelper copyStudentDataHelper;

    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);

        // Return query mock for any createNativeQuery call
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);

        // Return a dummy update count
        when(query.executeUpdate()).thenReturn(1);

        copyStudentDataHelper = new CopyStudentDataHelper(entityManager);
    }

    @Test
    public void testCopyStudentsTable() {
        copyStudentDataHelper.copyStudentsTable();

        verify(entityManager).createNativeQuery(
                "INSERT INTO locked_enrolled_students SELECT * from enrolled_students"
        );
        verify(query).executeUpdate();
    }

    @Test
    public void testClearLockedStudentsTable() {
        copyStudentDataHelper.clearLockedStudentsTable();

        verify(entityManager).createNativeQuery(
                "DELETE FROM locked_enrolled_students"
        );
        verify(query).executeUpdate();
    }
}

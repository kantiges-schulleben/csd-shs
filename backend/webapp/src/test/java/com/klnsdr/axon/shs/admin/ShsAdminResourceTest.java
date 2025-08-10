package com.klnsdr.axon.shs.admin;

import com.klnsdr.axon.shs.StudentDTO;
import com.klnsdr.axon.shs.TeacherDTO;
import com.klnsdr.axon.shs.entity.EnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.LockedEnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.Student;
import com.klnsdr.axon.shs.entity.Teacher;
import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import com.klnsdr.axon.shs.service.AnalysisConfigService;
import com.klnsdr.axon.shs.service.ShsConfigService;
import com.klnsdr.axon.shs.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ShsAdminResourceTest {
    private StudentService studentService;
    private AnalysisConfigService analysisConfigService;
    private ShsConfigService shsConfigService;
    private ShsAdminResource resource;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        analysisConfigService = mock(AnalysisConfigService.class);
        shsConfigService = mock(ShsConfigService.class);
        resource = new ShsAdminResource(studentService, analysisConfigService, shsConfigService);
    }

    @Test
    void testGetStudentCount() {
        when(studentService.getEnrolledStudentCount()).thenReturn(42L);
        assertEquals(42L, resource.getStudentCount());
        verify(studentService).getEnrolledStudentCount();
    }

    @Test
    void testSearchStudents() {
        List<EnrolledStudentEntity> students = List.of(new EnrolledStudentEntity());
        when(studentService.searchByName("abc")).thenReturn(students);
        assertEquals(students, resource.searchStudents("abc"));
    }

    @Test
    void testUpdateStudentDetails() {
        StudentDTO dto = new StudentDTO();
        EnrolledStudentEntity updated = new EnrolledStudentEntity();
        Student student = dto.toStudent();
        student.setId(1L);
        when(studentService.update(any(Student.class))).thenReturn(updated);

        EnrolledStudentEntity result = resource.updateStudentDetails(dto, 1L);
        assertEquals(updated, result);
        verify(studentService).update(any(Student.class));
    }

    @Test
    void testUpdateTeacherDetails() {
        TeacherDTO dto = new TeacherDTO();
        EnrolledStudentEntity updated = new EnrolledStudentEntity();
        Teacher teacher = dto.toTeacher();
        teacher.setId(2L);
        when(studentService.update(any(Teacher.class))).thenReturn(updated);

        EnrolledStudentEntity result = resource.updateTeacherDetails(dto, 2L);
        assertEquals(updated, result);
        verify(studentService).update(any(Teacher.class));
    }

    @Test
    void testDeleteStudentFound() {
        EnrolledStudentEntity deleted = new EnrolledStudentEntity();
        when(studentService.delete(1L)).thenReturn(deleted);

        ResponseEntity<?> response = resource.deleteStudent(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteStudentNotFound() {
        when(studentService.delete(1L)).thenReturn(null);

        ResponseEntity<?> response = resource.deleteStudent(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetStudentsBySubject() {
        List<LockedEnrolledStudentEntity> list = List.of(new LockedEnrolledStudentEntity());
        when(studentService.getStudentsBySubject("math")).thenReturn(list);

        assertEquals(list, resource.getStudentsBySubject("math"));
    }

    @Test
    void testGetTeachersBySubject() {
        List<LockedEnrolledStudentEntity> list = List.of(new LockedEnrolledStudentEntity());
        when(studentService.getTeachersBySubject("science")).thenReturn(list);

        assertEquals(list, resource.getTeachersBySubject("science"));
    }

    @Test
    void testStartAnalysis() {
        doNothing().when(studentService).runAnalysis();

        ResponseEntity<?> response = resource.startAnalysis();
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(studentService).runAnalysis();
    }

    @Test
    void testGetIsAnalysisRunning() {
        when(studentService.isAnalysisRunning()).thenReturn(true);
        ResponseEntity<Boolean> response = resource.getIsAnalysisRunning();
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testGetAnalysisStatus() {
        Pair<Boolean, String> pair = Pair.of(true, "ok");
        when(analysisConfigService.getResult()).thenReturn(pair);

        assertEquals(pair, resource.getAnalysisStatus());
    }

    @Test
    void testIsPhaseTwo() {
        when(analysisConfigService.isPhaseTwo()).thenReturn(true);

        ResponseEntity<Boolean> response = resource.isPhaseTwo();
        assertEquals(Boolean.TRUE, response.getBody());
    }

    @Test
    void testResetAnalysisSuccess() {
        when(studentService.resetData()).thenReturn(true);
        ResponseEntity<?> response = resource.resetAnalysis();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testResetAnalysisFailure() {
        when(studentService.resetData()).thenReturn(false);
        ResponseEntity<?> response = resource.resetAnalysis();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetSinglePairs() {
        List<Group> groups = List.of(new Group());
        when(studentService.getSinglePairs()).thenReturn(groups);

        assertEquals(groups, resource.getSinglePairs());
    }

    @Test
    void testGetGroupPairs() {
        List<Group> groups = List.of(new Group());
        when(studentService.getGroupPairs()).thenReturn(groups);

        assertEquals(groups, resource.getGroupPairs());
    }

    @Test
    void testGetWithoutPartner() {
        List<LockedEnrolledStudentEntity> list = List.of(new LockedEnrolledStudentEntity());
        when(studentService.getWithoutPartner()).thenReturn(list);

        assertEquals(list, resource.getWithoutPartner());
    }

    @Test
    void testReleaseGroupSuccess() {
        when(studentService.releaseGroup(1L)).thenReturn(true);

        ResponseEntity<?> response = resource.releaseGroup(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testReleaseGroupNotFound() {
        when(studentService.releaseGroup(1L)).thenReturn(false);

        ResponseEntity<?> response = resource.releaseGroup(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteGroupSuccess() {
        when(studentService.deleteGroup(1L)).thenReturn(true);

        ResponseEntity<?> response = resource.deleteGroup(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteGroupNotFound() {
        when(studentService.deleteGroup(1L)).thenReturn(false);

        ResponseEntity<?> response = resource.deleteGroup(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteGroupIllegalStateException() {
        when(studentService.deleteGroup(1L)).thenThrow(new IllegalStateException("Forbidden"));

        ResponseEntity<?> response = resource.deleteGroup(1L);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Forbidden", response.getBody());
    }

    @Test
    void testCreateGroupSuccess() {
        CreateGroupDTO dto = new CreateGroupDTO();
        dto.setSubject("math");
        dto.setTeacherId(1L);
        dto.setStudentId(2L);
        Group group = new Group();
        when(studentService.createGroup(1L, 2L, "math")).thenReturn(group);

        Group result = resource.createGroup(dto);
        assertEquals(group, result);
    }

    @Test
    void testCreateGroupInvalidArgument() {
        CreateGroupDTO dto = new CreateGroupDTO();
        dto.setSubject("math");
        dto.setTeacherId(1L);
        dto.setStudentId(2L);
        when(studentService.createGroup(1L, 2L, "math")).thenThrow(new IllegalArgumentException("bad args"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> resource.createGroup(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("bad args", ex.getReason());
    }

    @Test
    void testSetEnrollEndDateSuccess() {
        UpdateEnrollEndDateDTO dto = new UpdateEnrollEndDateDTO();
        dto.setEnrollEndDate(new Date());
        doNothing().when(shsConfigService).setEnrollEndDate(dto.getEnrollEndDate());

        ResponseEntity<?> response = resource.setEnrollEndDate(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSetEnrollEndDateInvalidArgument() {
        UpdateEnrollEndDateDTO dto = new UpdateEnrollEndDateDTO();
        dto.setEnrollEndDate(new Date());
        doThrow(new IllegalArgumentException("invalid date")).when(shsConfigService).setEnrollEndDate(dto.getEnrollEndDate());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> resource.setEnrollEndDate(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("invalid date", ex.getReason());
    }
}

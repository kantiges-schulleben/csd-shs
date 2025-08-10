package com.klnsdr.axon.shs;

import com.klnsdr.axon.shs.entity.Student;
import com.klnsdr.axon.shs.entity.Teacher;
import com.klnsdr.axon.shs.service.ShsConfigService;
import com.klnsdr.axon.shs.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class ShsResourceTest {
    private StudentService studentService;
    private ShsConfigService shsConfigService;
    private ShsResource shsResource;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        shsConfigService = mock(ShsConfigService.class);
        shsResource = new ShsResource(studentService, shsConfigService);
    }

    @Test
    void enrollStudent_shouldCallCreateStudentWithConvertedStudent() {
        StudentDTO dto = new StudentDTO();
        dto.setName("John");
        dto.setSureName("Doe");
        dto.setMail("john.doe@example.com");
        dto.setSubject("Math");
        dto.setGrade(10);
        dto.setGroup(true);
        dto.setPhoneNumber("1234567890");

        Student createdStudent = new Student();
        when(studentService.createStudent(any(Student.class))).thenReturn(createdStudent);

        Student result = shsResource.enrollStudent(dto);

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentService).createStudent(captor.capture());
        Student passedStudent = captor.getValue();

        assertEquals(dto.getName(), passedStudent.getName());
        assertEquals(dto.getSureName(), passedStudent.getSureName());
        assertEquals(dto.getMail(), passedStudent.getMail());
        assertEquals(dto.getSubject(), passedStudent.getSubject());
        assertEquals(dto.getGrade(), passedStudent.getGrade());
        assertEquals(dto.isGroup(), passedStudent.isGroup());
        assertEquals(dto.getPhoneNumber(), passedStudent.getPhoneNumber());

        assertSame(createdStudent, result);
    }

    @Test
    void enrollTeacher_shouldCallCreateTeacherWithConvertedTeacher() {
        TeacherDTO dto = new TeacherDTO();
        dto.setName("Jane");
        dto.setSureName("Smith");
        dto.setMail("jane.smith@example.com");
        dto.setSubject("Physics");
        dto.setGrade(11);
        dto.setGroup(false);
        dto.setPhoneNumber("0987654321");
        dto.setTargetGrade(12);

        Teacher createdTeacher = new Teacher();
        when(studentService.createTeacher(any(Teacher.class))).thenReturn(createdTeacher);

        Teacher result = shsResource.enrollTeacher(dto);

        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(studentService).createTeacher(captor.capture());
        Teacher passedTeacher = captor.getValue();

        assertEquals(dto.getName(), passedTeacher.getName());
        assertEquals(dto.getSureName(), passedTeacher.getSureName());
        assertEquals(dto.getMail(), passedTeacher.getMail());
        assertEquals(dto.getSubject(), passedTeacher.getSubject());
        assertEquals(dto.getGrade(), passedTeacher.getGrade());
        assertEquals(dto.isGroup(), passedTeacher.isGroup());
        assertEquals(dto.getPhoneNumber(), passedTeacher.getPhoneNumber());
        assertEquals(dto.getTargetGrade(), passedTeacher.getTargetGrade());

        assertSame(createdTeacher, result);
    }

    @Test
    void getEnrollEndDate_shouldReturnFormattedDateInCET() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-31");
        when(shsConfigService.getEnrollEndDate()).thenReturn(date);

        String result = shsResource.getEnrollEndDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String expected = sdf.format(date);

        assertEquals(expected, result);
    }
}

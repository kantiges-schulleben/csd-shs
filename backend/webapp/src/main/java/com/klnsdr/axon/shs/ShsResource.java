package com.klnsdr.axon.shs;

import com.klnsdr.axon.shs.entity.Student;
import com.klnsdr.axon.shs.entity.Teacher;
import com.klnsdr.axon.shs.service.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/shs")
public class ShsResource {
    private final StudentService studentService;

    public ShsResource(StudentService studentService) {
        this.studentService = studentService;
    }

    // TODO validate
    @PostMapping("/enroll/student")
    public Student enrollStudent(@RequestBody StudentDTO student, Principal principal) {
        return studentService.createStudent(map(student));
    }

    // TODO validate
    @PostMapping("/enroll/teacher")
    public Teacher enrollTeacher(@RequestBody TeacherDTO teacher, Principal principal) {
        return studentService.createTeacher(map(teacher));
    }


    private Student map(StudentDTO studentDTO) {
        final Student student = new Student();
        student.setName(studentDTO.getName());
        student.setSureName(studentDTO.getSureName());
        student.setMail(studentDTO.getMail());
        student.setSubject(studentDTO.getSubject());
        student.setGrade(studentDTO.getGrade());
        student.setGroup(studentDTO.isGroup());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        return student;
    }

    private Teacher map(TeacherDTO teacherDTO) {
        final Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.getName());
        teacher.setSureName(teacherDTO.getSureName());
        teacher.setMail(teacherDTO.getMail());
        teacher.setSubject(teacherDTO.getSubject());
        teacher.setGrade(teacherDTO.getGrade());
        teacher.setGroup(teacherDTO.isGroup());
        teacher.setPhoneNumber(teacherDTO.getPhoneNumber());
        teacher.setTargetGrade(teacherDTO.getTargetGrade());
        return teacher;
    }
}

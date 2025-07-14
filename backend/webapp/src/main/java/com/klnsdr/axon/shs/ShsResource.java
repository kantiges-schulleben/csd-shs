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
    public Student enrollStudent(@RequestBody StudentDTO student) {
        return studentService.createStudent(StudentDTO.map(student));
    }

    // TODO validate
    @PostMapping("/enroll/teacher")
    public Teacher enrollTeacher(@RequestBody TeacherDTO teacher) {
        return studentService.createTeacher(TeacherDTO.map(teacher));
    }
}

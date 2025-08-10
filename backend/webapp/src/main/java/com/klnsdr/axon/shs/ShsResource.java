package com.klnsdr.axon.shs;

import com.klnsdr.axon.shs.entity.Student;
import com.klnsdr.axon.shs.entity.Teacher;
import com.klnsdr.axon.shs.service.ShsConfigService;
import com.klnsdr.axon.shs.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/shs")
public class ShsResource {
    private final StudentService studentService;
    private final ShsConfigService shsConfigService;

    public ShsResource(StudentService studentService, ShsConfigService shsConfigService) {
        this.studentService = studentService;
        this.shsConfigService = shsConfigService;
    }

    // TODO validate
    @PostMapping("/enroll/student")
    public Student enrollStudent(@RequestBody StudentDTO student) {
        return studentService.createStudent(student.toStudent());
    }

    // TODO validate
    @PostMapping("/enroll/teacher")
    public Teacher enrollTeacher(@RequestBody TeacherDTO teacher) {
        return studentService.createTeacher(teacher.toTeacher());
    }

    @GetMapping("/end-date")
    public String getEnrollEndDate() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("CET"));
        return dateFormat.format(shsConfigService.getEnrollEndDate());
    }
}

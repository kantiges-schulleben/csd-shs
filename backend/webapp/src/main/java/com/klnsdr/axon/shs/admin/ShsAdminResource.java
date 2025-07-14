package com.klnsdr.axon.shs.admin;

import com.klnsdr.axon.shs.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shs/admin")
public class ShsAdminResource {
    private final StudentService studentService;

    public ShsAdminResource(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students/count")
    public long getStudentCount() {
        return studentService.getEnrolledStudentCount();
    }

    @GetMapping("/students/search")
    public ResponseEntity<?> searchStudents() {
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<?> getStudentDetails() {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<?> updateStudentDetails() {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<?> deleteStudent() {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/start")
    public ResponseEntity<?> startAnalysis() {
        return ResponseEntity.status(501).build();
    }
}

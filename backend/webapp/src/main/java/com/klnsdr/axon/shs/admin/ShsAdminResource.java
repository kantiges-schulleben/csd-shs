package com.klnsdr.axon.shs.admin;

import com.klnsdr.axon.shs.entity.EnrolledStudentEntity;
import com.klnsdr.axon.shs.service.StudentService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<EnrolledStudentEntity> searchStudents(@RequestParam("q") String query) {
        return studentService.searchByName(query);
    }

    @GetMapping("/students/id/{studentId}")
    public ResponseEntity<?> getStudentDetails() {
        return ResponseEntity.status(501).build();
    }

    @PutMapping("/students/id/{studentId}")
    public ResponseEntity<?> updateStudentDetails() {
        return ResponseEntity.status(501).build();
    }

    @DeleteMapping("/students/id/{studentId}")
    public ResponseEntity<?> deleteStudent() {
        return ResponseEntity.status(501).build();
    }

    @PostMapping("/start")
    public ResponseEntity<?> startAnalysis() {
        return ResponseEntity.status(501).build();
    }
}

package com.klnsdr.axon.shs.admin;

import com.klnsdr.axon.shs.StudentDTO;
import com.klnsdr.axon.shs.TeacherDTO;
import com.klnsdr.axon.shs.entity.EnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.Student;
import com.klnsdr.axon.shs.entity.Teacher;
import com.klnsdr.axon.shs.service.StudentService;
import org.springframework.data.util.Pair;
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

    // TODO validate
    @PutMapping("/students/id/{studentId}")
    public EnrolledStudentEntity updateStudentDetails(@RequestBody StudentDTO studentEntity, @PathVariable("studentId") Long studentId) {
        final Student student = StudentDTO.map(studentEntity);
        student.setId(studentId);
        return studentService.update(student);
    }

    // TODO validate
    @PutMapping("/teachers/id/{teacherId}")
    public EnrolledStudentEntity updateTeacherDetails(@RequestBody TeacherDTO teacherEntity, @PathVariable("teacherId") Long teacherId) {
        final Teacher teacher = TeacherDTO.map(teacherEntity);
        teacher.setId(teacherId);
        return studentService.update(teacher);
    }

    @DeleteMapping("/students/id/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") Long studentId) {
        final EnrolledStudentEntity deletedEntity = studentService.delete(studentId);
        if (deletedEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/start")
    public ResponseEntity<?> startAnalysis() {
        studentService.runAnalysis();
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/analysis/running")
    public ResponseEntity<Boolean> getIsAnalysisRunning() {
        final boolean status = studentService.isAnalysisRunning();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/analysis/status")
    public Pair<Boolean, String> getAnalysisStatus() {
        return studentService.getAnalysisStatus();
    }
}

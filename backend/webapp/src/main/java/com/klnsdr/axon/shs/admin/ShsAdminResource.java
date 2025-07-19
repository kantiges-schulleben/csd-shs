package com.klnsdr.axon.shs.admin;

import com.klnsdr.axon.shs.StudentDTO;
import com.klnsdr.axon.shs.TeacherDTO;
import com.klnsdr.axon.shs.entity.EnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.LockedEnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.Student;
import com.klnsdr.axon.shs.entity.Teacher;
import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import com.klnsdr.axon.shs.service.AnalysisConfigService;
import com.klnsdr.axon.shs.service.StudentService;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shs/admin")
public class ShsAdminResource {
    private final StudentService studentService;
    private final AnalysisConfigService analysisConfigService;

    public ShsAdminResource(StudentService studentService, AnalysisConfigService analysisConfigService) {
        this.studentService = studentService;
        this.analysisConfigService = analysisConfigService;
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
        return analysisConfigService.getResult();
    }

    @GetMapping("/is-phase-two")
    public ResponseEntity<Boolean> isPhaseTwo() {
        boolean isPhaseTwo = analysisConfigService.isPhaseTwo();
        return ResponseEntity.ok(isPhaseTwo);
    }

    @PutMapping("/reset")
    public ResponseEntity<?> resetAnalysis() {
        if (studentService.resetData()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/pairs/single")
    public List<Group> getSinglePairs() {
        return studentService.getSinglePairs();
    }

    @GetMapping("/pairs/group")
    public List<Group> getGroupPairs() {
        return studentService.getGroupPairs();
    }

    @GetMapping("/pairs/without")
    public List<LockedEnrolledStudentEntity> getWithoutPartner() {
        return studentService.getWithoutPartner();
    }

    @PutMapping("/pairs/id/{id}/release")
    public ResponseEntity<?> releaseGroup(@PathVariable("id") Long id) {
        if (studentService.releaseGroup(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

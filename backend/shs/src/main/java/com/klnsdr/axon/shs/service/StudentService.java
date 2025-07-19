package com.klnsdr.axon.shs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klnsdr.axon.shs.entity.*;
import com.klnsdr.axon.shs.entity.analysis.AnalysisResultEntity;
import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import com.klnsdr.axon.shs.service.legacy.GroupRepository;
import com.klnsdr.axon.shs.service.legacy.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final LockedStudentRepository lockedStudentRepository;
    private final CopyStudentDataHelper copyStudentDataHelper;
    private final AnalysisResultRepository analysisResultRepository;
    private final GroupService groupService;

    public StudentService(
            StudentRepository studentRepository,
            LockedStudentRepository lockedStudentRepository,
            CopyStudentDataHelper copyStudentDataHelper,
            AnalysisResultRepository analysisResultRepository,
            GroupService groupService
    ) {
        this.studentRepository = studentRepository;
        this.lockedStudentRepository = lockedStudentRepository;
        this.copyStudentDataHelper = copyStudentDataHelper;
        this.analysisResultRepository = analysisResultRepository;
        this.groupService = groupService;
    }

    public Teacher createTeacher(Teacher teacher) {
        final EnrolledStudentEntity entity = map(teacher);
        return mapToTeacher(studentRepository.save(entity));
    }

    public Student createStudent(Student student) {
        final EnrolledStudentEntity entity = map(student);
        return mapToStudent(studentRepository.save(entity));
    }

    public long getEnrolledStudentCount() {
        return studentRepository.count();
    }

    public List<EnrolledStudentEntity> searchByName(String query) {
        if (query == null) {
            return List.of();
        }
        return studentRepository.findByNameContainingIgnoreCase(query);
    }

    public EnrolledStudentEntity update(Teacher teacher) {
        if (teacher == null || teacher.getId() == null) {
            throw new IllegalArgumentException("Teacher must not be null and must have an ID");
        }

        return studentRepository.save(map(teacher));
    }

    public EnrolledStudentEntity update(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("Student must not be null and must have an ID");
        }

        return studentRepository.save(map(student));
    }

    public EnrolledStudentEntity delete(long id) {
        final Optional<EnrolledStudentEntity> entity = studentRepository.findById(id);
        if (entity.isEmpty()) {
            return null;
        }
        final EnrolledStudentEntity entityToDelete = entity.get();
        studentRepository.delete(entityToDelete);
        return entityToDelete;
    }

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Async
    public void runAnalysis() {
        if (!running.compareAndSet(false, true)) {
            logger.warn("Task already running");
            return;
        }

        try {
            runAnalysisInternal();
        } catch (Exception e) {
            logger.error("Unexpected error during analysis", e);
            writeAnalysisStatusToDatabase(false, "Unexpected error: " + e.getMessage());
            running.set(false);
        }
    }

    private void runAnalysisInternal() {
        groupService.deleteAllGroupsAndStudents();

        final boolean didClear = copyStudentDataHelper.clearLockedStudentsTable();
        if (!didClear) {
            logger.error("Failed to clear locked students table");
            writeAnalysisStatusToDatabase(false, "Failed to clear locked students table");
            running.set(false);
            return;
        }

        final boolean didCopy = copyStudentDataHelper.copyStudentsTable();
        if (!didCopy) {
            logger.error("Failed to copy students table");
            writeAnalysisStatusToDatabase(false, "Failed to copy students table");
            running.set(false);
            return;
        }

        final List<LockedEnrolledStudentEntity> allEnrolledStudents = lockedStudentRepository.findAll();
        final String studentsAsParam = "[" + allEnrolledStudents.stream()
                .map(LockedEnrolledStudentEntity::asJson)
                .reduce((a, b) -> a + "," + b)
                .orElse("") + "]";

        if (studentsAsParam.equals("[]")) {
            logger.warn("No students found for analysis");
            writeAnalysisStatusToDatabase(false, "No students found for analysis");
            running.set(false);
            return;
        }

        final Pair<Boolean, String> result = new AnalysisScriptRunner().runAnalysisScript(studentsAsParam);
        if (!result.getFirst()) {
            logger.error("Analysis script failed: {}", result.getSecond());
            writeAnalysisStatusToDatabase(false, result.getSecond().length() < 20 ? result.getSecond() : "Ein Fehler beim Ausführen des Skripts ist aufgetreten");
            running.set(false);
            return;
        }

        try {
            parseAndStoreScriptOutput(result.getSecond());
        } catch (Exception e) {
            logger.error("Failed to parse and store script output", e);
            writeAnalysisStatusToDatabase(false, "Failed to parse and store script output: " + e.getMessage());
            running.set(false);
            return;
        }

        writeAnalysisStatusToDatabase(true, "");
        running.set(false);
    }

    public Pair<Boolean, String> getAnalysisStatus() {
        final List<AnalysisResultEntity> results = analysisResultRepository.findAll();
        if (results.isEmpty()) {
            return Pair.of(false, "No analysis results found");
        }
        final AnalysisResultEntity latestResult = results.getLast();
        return Pair.of(latestResult.isWasSuccessful(), latestResult.getErrorMessage());
    }

    private void writeAnalysisStatusToDatabase(boolean status, String message) {
        final AnalysisResultEntity result = new AnalysisResultEntity();
        result.setWasSuccessful(status);
        result.setErrorMessage(message);
        analysisResultRepository.save(result);
    }

    public boolean isAnalysisRunning() {
        return running.get();
    }

    private EnrolledStudentEntity map(Teacher teacher) {
        final EnrolledStudentEntity entity = new EnrolledStudentEntity();
        entity.setId(teacher.getId());
        entity.setName(teacher.getName());
        entity.setSureName(teacher.getSureName());
        entity.setMail(teacher.getMail());
        entity.setSubject(teacher.getSubject());
        entity.setGrade(teacher.getGrade());
        entity.setGroup(teacher.isGroup());
        entity.setPhoneNumber(teacher.getPhoneNumber());
        entity.setTargetGrade(teacher.getTargetGrade());
        entity.setTeacher(true);
        return entity;
    }

    private EnrolledStudentEntity map(Student student) {
        final EnrolledStudentEntity entity = new EnrolledStudentEntity();
        entity.setId(student.getId());
        entity.setName(student.getName());
        entity.setSureName(student.getSureName());
        entity.setMail(student.getMail());
        entity.setSubject(student.getSubject());
        entity.setGrade(student.getGrade());
        entity.setGroup(student.isGroup());
        entity.setPhoneNumber(student.getPhoneNumber());
        entity.setTargetGrade(0); // default
        entity.setTeacher(false);
        return entity;
    }

    private Student mapToStudent(EnrolledStudentEntity entity) {
        Student student = new Student();
        student.setId(entity.getId());
        student.setName(entity.getName());
        student.setSureName(entity.getSureName());
        student.setMail(entity.getMail());
        student.setSubject(entity.getSubject());
        student.setGrade(entity.getGrade());
        student.setGroup(entity.isGroup());
        student.setPhoneNumber(entity.getPhoneNumber());
        return student;
    }

    private Teacher mapToTeacher(EnrolledStudentEntity entity) {
        Teacher teacher = new Teacher();
        teacher.setId(entity.getId());
        teacher.setName(entity.getName());
        teacher.setSureName(entity.getSureName());
        teacher.setMail(entity.getMail());
        teacher.setSubject(entity.getSubject());
        teacher.setGrade(entity.getGrade());
        teacher.setGroup(entity.isGroup());
        teacher.setPhoneNumber(entity.getPhoneNumber());
        teacher.setTargetGrade(entity.getTargetGrade());
        return teacher;
    }

    private void parseAndStoreScriptOutput(String output) throws JsonProcessingException {
        @SuppressWarnings("unchecked")
        final HashMap<String, Object> resultMap = new ObjectMapper().readValue(output, HashMap.class);

        if (!(
                resultMap.containsKey("einzel") &&
                resultMap.containsKey("gruppe") &&
                resultMap.containsKey("ohne") &&
                resultMap.get("einzel") instanceof List &&
                resultMap.get("gruppe") instanceof List &&
                resultMap.get("ohne") instanceof List
        )) {
            logger.error("Script output does not contain expected keys: {}", resultMap.keySet());
            throw new IllegalArgumentException("Invalid script output format");
        }
        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> single = (List<Map<String, Object>>) resultMap.get("einzel");
        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> group = (List<Map<String, Object>>) resultMap.get("gruppe");
        @SuppressWarnings("unchecked")
        final List<Map<String, Object>> without = (List<Map<String, Object>>) resultMap.get("ohne");

        doSingle(single);
        doGroup(group);
        doWithout(without);
    }

    private void doSingle(List<Map<String, Object>> data) {
        // TODO more validation
        data.forEach(pair -> {
            final Group group = new Group();
            group.setSubject((String) pair.get("facher"));
            group.setReleased(false);
            group.setTeacher(lockedStudentRepository.findById(Long.parseLong((String) pair.get("accountID")))
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found for ID: " + pair.get("accountID"))));
            final LockedEnrolledStudentEntity student = lockedStudentRepository.findById(Long.parseLong((String) pair.get("accountID")))
                    .orElseThrow(() -> new IllegalArgumentException("Student not found for ID: " + pair.get("accountID")));
            group.setStudents(List.of(student));

            groupService.save(group);
        });
    }

    private void doGroup(List<Map<String, Object>> data) {

    }

    private void doWithout(List<Map<String, Object>> data) {

    }
}

package com.klnsdr.axon.shs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.klnsdr.axon.shs.entity.*;
import com.klnsdr.axon.shs.entity.analysis.AnalysisConfig;
import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import com.klnsdr.axon.shs.service.legacy.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final LockedStudentRepository lockedStudentRepository;
    private final CopyStudentDataHelper copyStudentDataHelper;
    private final AnalysisConfigService analysisConfigService;
    private final GroupService groupService;

    public StudentService(
            StudentRepository studentRepository,
            LockedStudentRepository lockedStudentRepository,
            CopyStudentDataHelper copyStudentDataHelper,
            GroupService groupService,
            AnalysisConfigService analysisConfigService

    ) {
        this.studentRepository = studentRepository;
        this.lockedStudentRepository = lockedStudentRepository;
        this.copyStudentDataHelper = copyStudentDataHelper;
        this.groupService = groupService;
        this.analysisConfigService = analysisConfigService;
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

    public List<Group> getSinglePairs() {
        return groupService.getSinglePairs();
    }

    public List<Group> getGroupPairs() {
        return groupService.getGroupPairs();
    }

    public List<LockedEnrolledStudentEntity> getWithoutPartner() {
        return lockedStudentRepository.getWithoutPartner();
    }

    public boolean releaseGroup(Long id) {
        final Optional<Group> group = groupService.findById(id);
        if (group.isEmpty()) {
            logger.warn("Group with ID {} not found", id);
            return false;
        }

        final Group existingGroup = group.get();
        existingGroup.setReleased(true);
        groupService.save(existingGroup);
        return true;
    }

    public boolean deleteGroup(Long id) {
        final Optional<Group> deletedGroup = groupService.deleteById(id);
        if (deletedGroup.isEmpty()) {
            logger.warn("Group with ID {} not found", id);
            return false;
        }

        return true;
    }

    public List<LockedEnrolledStudentEntity> getStudentsBySubject(String subject) {
        if (subject == null || subject.isEmpty()) {
            return List.of();
        }
        return lockedStudentRepository.findBySubjectAndIsTeacherIsFalse(subject);
    }

    public List<LockedEnrolledStudentEntity> getTeachersBySubject(String subject) {
        if (subject == null || subject.isEmpty()) {
            return List.of();
        }
        return lockedStudentRepository.findBySubjectAndIsTeacherIsTrue(subject);
    }

    public Group createGroup(Long teacherId, Long studentId, String subject) {
        final Optional<LockedEnrolledStudentEntity> teacherOpt = lockedStudentRepository.findById(teacherId);
        if (teacherOpt.isEmpty() || !teacherOpt.get().isTeacher()) {
            throw new IllegalArgumentException("Invalid teacher ID: " + teacherId);
        }

        final Optional<LockedEnrolledStudentEntity> studentOpt = lockedStudentRepository.findById(studentId);
        if (studentOpt.isEmpty() || studentOpt.get().isTeacher()) {
            throw new IllegalArgumentException("Invalid student ID: " + studentId);
        }

        final Group group = new Group();
        group.setTeacher(teacherOpt.get());
        group.setStudents(List.of(studentOpt.get()));
        group.setSubject(subject);
        group.setReleased(false);

        groupService.save(group);
        return group;
    }

    public boolean resetData() {
        try {
//            studentRepository.deleteAll();
            groupService.deleteAllGroupsAndStudents();

            final boolean didClear = copyStudentDataHelper.clearLockedStudentsTable();
            if (!didClear) {
                logger.error("Failed to clear locked students table");
            }

            final boolean didCopy = copyStudentDataHelper.copyStudentsTable();
            if (!didCopy) {
                logger.error("Failed to copy students table");
            }

            analysisConfigService.reset();
        } catch (Exception e) {
            logger.error("Unexpected error during data reset", e);
            return false;
        }
        return true;
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
            analysisConfigService.setPhaseTwo(true);
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

    private void writeAnalysisStatusToDatabase(boolean status, String message) {
        analysisConfigService.setResult(status, message);
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

        doSingle(single);
        doGroup(group);
    }

    private void doSingle(List<Map<String, Object>> data) {
        // TODO more validation
        data.forEach(pair -> {
            final Group group = new Group();
            group.setSubject((String) pair.get("facher"));
            group.setReleased(false);
            group.setTeacher(lockedStudentRepository.findById(Long.parseLong((String) pair.get("accountID")))
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found for ID: " + pair.get("accountID"))));
            final LockedEnrolledStudentEntity student = lockedStudentRepository.findById(Long.parseLong((String)((Map<String, Object>) pair.get("partner")).get("accountID")))
                    .orElseThrow(() -> new IllegalArgumentException("Student not found for ID: " + pair.get("accountID")));
            group.setStudents(List.of(student));

            groupService.save(group);
        });
    }

    private void doGroup(List<Map<String, Object>> data) {
        // TODO more validation
        data.forEach(pair -> {
            final Group group = new Group();
            group.setSubject((String) pair.get("facher"));
            group.setReleased(false);
            group.setTeacher(lockedStudentRepository.findById(Long.parseLong((String) pair.get("accountID")))
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found for ID: " + pair.get("accountID"))));

            final List<LockedEnrolledStudentEntity> students = new ArrayList<>();
            final int studentCount = Integer.parseInt((String) pair.get("anzahlPartner"));

            final Map<String, Object> studentData = (Map<String, Object>) pair.get("partner");
            for (int i = 1; i <= studentCount; i++) {
                final LockedEnrolledStudentEntity student = lockedStudentRepository.findById(Long.parseLong((String)((Map<String, Object>) studentData.get(Integer.toString(i))).get("accountID")))
                        .orElseThrow(() -> new IllegalArgumentException("Student not found for ID: " + studentData.get("accountID")));
                students.add(student);
            }

            group.setStudents(students);

            groupService.save(group);
        });
    }
}

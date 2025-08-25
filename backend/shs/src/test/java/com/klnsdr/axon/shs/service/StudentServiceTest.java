package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.mail.Mail;
import com.klnsdr.axon.mail.service.MailService;
import com.klnsdr.axon.shs.entity.EnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.LockedEnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.Teacher;
import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import com.klnsdr.axon.shs.mail.ShsMailRenderer;
import com.klnsdr.axon.shs.mail.ShsMailStore;
import com.klnsdr.axon.shs.service.legacy.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentServiceTest {
    private StudentRepository studentRepository;
    private LockedStudentRepository lockedStudentRepository;
    private CopyStudentDataHelper copyStudentDataHelper;
    private GroupService groupService;
    private AnalysisConfigService analysisConfigService;
    private AnalysisScriptRunner analysisScriptRunner;
    private MailService mailService;
    private ShsMailStore shsMailStore;
    private ShsMailRenderer shsMailRenderer;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        lockedStudentRepository = mock(LockedStudentRepository.class);
        copyStudentDataHelper = mock(CopyStudentDataHelper.class);
        groupService = mock(GroupService.class);
        analysisConfigService = mock(AnalysisConfigService.class);
        analysisScriptRunner = mock(AnalysisScriptRunner.class);
        mailService = mock(MailService.class);
        shsMailStore = mock(ShsMailStore.class);
        shsMailRenderer = mock(ShsMailRenderer.class);

        studentService = new StudentService(
                studentRepository,
                lockedStudentRepository,
                copyStudentDataHelper,
                groupService,
                analysisConfigService,
                analysisScriptRunner,
                mailService,
                shsMailStore,
                shsMailRenderer
        );
    }

    @Test
    void createTeacher_savesMappedEntity() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Alice");

        EnrolledStudentEntity savedEntity = new EnrolledStudentEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Alice");

        when(studentRepository.save(any())).thenReturn(savedEntity);
        when(shsMailStore.getConfirmationMailTemplate()).thenReturn(new Mail());

        Teacher result = studentService.createTeacher(teacher);

        assertEquals("Alice", result.getName());
        verify(studentRepository).save(argThat(e -> e.isTeacher() && e.getId().equals(1L)));
        verify(shsMailStore).getConfirmationMailTemplate();
        verify(shsMailRenderer).renderMail(any(), any());
        verify(mailService, times(1)).sendEmail(any());
    }

    @Test
    void getEnrolledStudentCount_returnsCount() {
        when(studentRepository.count()).thenReturn(42L);
        assertEquals(42L, studentService.getEnrolledStudentCount());
    }

    @Test
    void releaseGroup_returnsTrueWhenGroupExists() {
        final LockedEnrolledStudentEntity teacher = new LockedEnrolledStudentEntity();
        teacher.setName("Alice");
        teacher.setSureName("Smith");
        teacher.setMail("alice@mail.com");
        teacher.setGrade(0);
        teacher.setPhoneNumber("0987654321");

        final LockedEnrolledStudentEntity student = new LockedEnrolledStudentEntity();
        student.setName("Bob");
        student.setSureName("Johnson");
        student.setMail("bob@mail.com");
        student.setPhoneNumber("1234567890");

        Group group = new Group();
        group.setTeacher(teacher);
        group.setStudents(List.of(student));
        group.setReleased(false);
        group.setSubject("");

        when(groupService.findById(1L)).thenReturn(Optional.of(group));
        when(shsMailStore.getPairTeacherMailTemplate()).thenReturn(new Mail());
        when(shsMailStore.getPairStudentMailTemplate()).thenReturn(new Mail());
        when(shsMailRenderer.renderMail(any(), any())).thenReturn(new Mail());

        boolean result = studentService.releaseGroup(1L);

        assertTrue(result);
        assertTrue(group.isReleased());
        verify(groupService).save(group);
        verify(shsMailStore).getPairTeacherMailTemplate();
        verify(shsMailStore).getPairStudentMailTemplate();
        verify(shsMailRenderer, times(2)).renderMail(any(), any());
        verify(mailService, times(2)).sendEmail(any());
    }

    @Test
    void releaseGroup_returnsFalseWhenMissing() {
        when(groupService.findById(1L)).thenReturn(Optional.empty());
        assertFalse(studentService.releaseGroup(1L));
        verify(groupService, never()).save(any());
    }

    @Test
    void resetData_executesDependencies() {
        boolean result = studentService.resetData();
        assertTrue(result);
        verify(groupService).deleteAllGroupsAndStudents();
        verify(copyStudentDataHelper).clearLockedStudentsTable();
        verify(copyStudentDataHelper).copyStudentsTable();
        verify(analysisConfigService).reset();
    }

    @Test
    void runAnalysisInternal_successfulPath() {
        LockedEnrolledStudentEntity student = mock(LockedEnrolledStudentEntity.class);
        when(student.asJson()).thenReturn("{\"id\":1}");
        when(lockedStudentRepository.findAll()).thenReturn(List.of(student));
        when(analysisScriptRunner.runAnalysisScript(anyString()))
                .thenReturn(Pair.of(true, "{\"einzel\":[],\"gruppe\":[],\"ohne\":[]}"));

        boolean result = invokeRunAnalysisInternal();

        assertTrue(result);
        verify(analysisConfigService).setResult(true, "");
    }

    @Test
    void runAnalysisInternal_failsWhenNoStudents() {
        when(lockedStudentRepository.findAll()).thenReturn(List.of());

        boolean result = invokeRunAnalysisInternal();

        assertFalse(result);
        verify(analysisConfigService).setResult(false, "No students found for analysis");
    }

    @Test
    void runAnalysisInternal_failsWhenScriptReturnsError() {
        LockedEnrolledStudentEntity student = mock(LockedEnrolledStudentEntity.class);
        when(student.asJson()).thenReturn("{\"id\":1}");
        when(lockedStudentRepository.findAll()).thenReturn(List.of(student));
        when(analysisScriptRunner.runAnalysisScript(anyString()))
                .thenReturn(Pair.of(false, "Script error"));

        boolean result = invokeRunAnalysisInternal();

        assertFalse(result);
        verify(analysisConfigService).setResult(false, "Script error");
    }

    private boolean invokeRunAnalysisInternal() {
        try {
            var m = StudentService.class.getDeclaredMethod("runAnalysisInternal");
            m.setAccessible(true);
            return (boolean) m.invoke(studentService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.klnsdr.axon.shs.service.legacy;

import com.klnsdr.axon.shs.entity.LockedEnrolledStudentEntity;
import com.klnsdr.axon.shs.entity.analysis.legacy.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupServiceTest {
    private GroupService groupService;
    private GroupRepository mockGroupRepository;

    @BeforeEach
    public void setUp() {
        mockGroupRepository = mock(GroupRepository.class);
        groupService = new GroupService(mockGroupRepository);
    }

    @Test
    public void saveGroupSuccessfullySavesGroup() {
        final Group group = new Group();
        group.setId(1L);

        groupService.save(group);

        verify(mockGroupRepository, times(1)).save(group);
    }

    @Test
    public void deleteAllGroupsAndStudentsSuccessfullyDeletesGroupsAndStudents() {
        final Group group1 = new Group();
        group1.setId(1L);
        group1.setStudents(List.of(new LockedEnrolledStudentEntity()));

        final Group group2 = new Group();
        group2.setId(2L);
        group2.setStudents(List.of(new LockedEnrolledStudentEntity()));

        when(mockGroupRepository.findAll()).thenReturn(List.of(group1, group2));

        groupService.deleteAllGroupsAndStudents();

        verify(mockGroupRepository, times(1)).findAll();
        verify(mockGroupRepository, times(1)).saveAll(any());
        verify(mockGroupRepository, times(1)).deleteAll();
    }

    @Test
    public void getSinglePairsReturnsSinglePairGroups() {
        final Group group1 = new Group();
        group1.setId(1L);

        final Group group2 = new Group();
        group2.setId(2L);

        when(mockGroupRepository.getSinglePairs()).thenReturn(List.of(group1, group2));

        List<Group> result = groupService.getSinglePairs();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(mockGroupRepository, times(1)).getSinglePairs();
    }

    @Test
    public void getGroupPairsReturnsGroupPairGroups() {
        final Group group1 = new Group();
        group1.setId(1L);

        final Group group2 = new Group();
        group2.setId(2L);

        when(mockGroupRepository.getGroupPairs()).thenReturn(List.of(group1, group2));

        List<Group> result = groupService.getGroupPairs();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(mockGroupRepository, times(1)).getGroupPairs();
    }

    @Test
    public void findByIdReturnsGroupWhenIdExists() {
        final Long groupId = 1L;
        final Group group = new Group();
        group.setId(groupId);

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Optional<Group> result = groupService.findById(groupId);
        assertTrue(result.isPresent());
        assertEquals(groupId, result.get().getId());

        verify(mockGroupRepository, times(1)).findById(groupId);
    }

    @Test
    public void findByIdReturnsEmptyWhenIdDoesNotExist() {
        final Long groupId = 1L;

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        Optional<Group> result = groupService.findById(groupId);
        assertFalse(result.isPresent());

        verify(mockGroupRepository, times(1)).findById(groupId);
    }

    @Test
    public void deleteByIdSuccessfullyDeletesGroupWhenNotReleased() {
        final Long groupId = 1L;
        final Group group = new Group();
        group.setId(groupId);
        group.setReleased(false);

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Optional<Group> result = groupService.deleteById(groupId);
        assertTrue(result.isPresent());
        assertEquals(groupId, result.get().getId());

        verify(mockGroupRepository, times(1)).findById(groupId);
        verify(mockGroupRepository, times(1)).save(group);
        verify(mockGroupRepository, times(1)).delete(group);
    }

    @Test
    public void deleteByIdThrowsExceptionWhenGroupIsReleased() {
        final Long groupId = 1L;
        final Group group = new Group();
        group.setId(groupId);
        group.setReleased(true);

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        assertThrows(IllegalStateException.class, () -> groupService.deleteById(groupId));

        verify(mockGroupRepository, times(1)).findById(groupId);
        verify(mockGroupRepository, never()).save(any());
        verify(mockGroupRepository, never()).delete(any());
    }

    @Test
    public void deleteByIdReturnsEmptyWhenGroupDoesNotExist() {
        final Long groupId = 1L;

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        Optional<Group> result = groupService.deleteById(groupId);
        assertFalse(result.isPresent());

        verify(mockGroupRepository, times(1)).findById(groupId);
        verify(mockGroupRepository, never()).save(any());
        verify(mockGroupRepository, never()).delete(any());
    }
}

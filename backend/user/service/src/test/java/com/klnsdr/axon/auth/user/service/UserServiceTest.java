package com.klnsdr.axon.auth.user.service;

import com.klnsdr.axon.user.entity.UserEntity;
import com.klnsdr.axon.user.service.UserRepository;
import com.klnsdr.axon.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void findByIdpIDReturnsUserWhenIdpIDExists() {
        final String idpID = "idp123";
        final UserEntity user = new UserEntity();
        user.setIdpID(idpID);

        when(userRepository.findByIdpIDEquals(idpID)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.findByIdpID(idpID);
        assertTrue(result.isPresent());
        assertEquals(idpID, result.get().getIdpID());

        verify(userRepository, times(1)).findByIdpIDEquals(idpID);
    }

    @Test
    public void findByIdpIDReturnsEmptyWhenIdpIDDoesNotExist() {
        final String idpID = "nonexistent";

        when(userRepository.findByIdpIDEquals(idpID)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findByIdpID(idpID);
        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByIdpIDEquals(idpID);
    }

    @Test
    public void createUserSuccessfullyCreatesAndReturnsUser() {
        final String idpID = "idp123";
        final String name = "John Doe";
        final UserEntity user = new UserEntity();
        user.setIdpID(idpID);
        user.setName(name);

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity result = userService.createUser(idpID, name);
        assertNotNull(result);
        assertEquals(idpID, result.getIdpID());
        assertEquals(name, result.getName());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void getAdminUserReturnsUserWhenAdminExists() {
        final UserEntity adminUser = new UserEntity();

        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        Optional<UserEntity> result = userService.getAdminUser();
        assertTrue(result.isPresent());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getAdminUserReturnsEmptyWhenAdminDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.getAdminUser();
        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void searchByNameReturnsMatchingUsers() {
        final String query = "John";
        final UserEntity user1 = new UserEntity();
        user1.setName("John Doe");
        final UserEntity user2 = new UserEntity();
        user2.setName("Johnny Appleseed");

        when(userRepository.findByNameContainingIgnoreCase(query)).thenReturn(List.of(user1, user2));

        List<UserEntity> result = userService.searchByName(query);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Johnny Appleseed", result.get(1).getName());

        verify(userRepository, times(1)).findByNameContainingIgnoreCase(query);
    }

    @Test
    public void deleteUserSuccessfullyDeletesAndReturnsUser() {
        final Long userId = 1L;
        final UserEntity user = new UserEntity();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        UserEntity result = userService.deleteUser(userId);
        assertNotNull(result);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteUserReturnsNullWhenUserDoesNotExist() {
        final Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserEntity result = userService.deleteUser(userId);
        assertNull(result);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}

package com.klnsdr.axon.user.service;

import com.klnsdr.axon.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> findByIdpID(String idpID) {
        return userRepository.findByIdpIDEquals(idpID);
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity createUser(String idpID, String name) {
        final UserEntity user = new UserEntity();
        user.setIdpID(idpID);
        user.setName(name);
        return userRepository.save(user);
    }

    public Optional<UserEntity> getAdminUser() {
        return userRepository.findById(1L);
    }

    public List<UserEntity> searchByName(String query) {
        return userRepository.findByNameContainingIgnoreCase(query);
    }

    public UserEntity deleteUser(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return user.get();
        }
        return null;
    }
}

package com.T1.CURD.Service;


import com.T1.CURD.DTO.UserDTO;
import com.T1.CURD.Entity.UserEntity;
import com.T1.CURD.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity(UUID.randomUUID(), userDTO.getName(), userDTO.getEmail(), userDTO.getAge());
        userEntity = userRepository.save(userEntity);
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getAge());
    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userEntity -> new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getAge()))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO getUserById(UUID id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getAge());
    }

    @Transactional
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userEntity.setName(userDTO.getName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setAge(userDTO.getAge());
        userEntity = userRepository.save(userEntity);
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getAge());
    }

    @Transactional
    public void deleteUser(UUID id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepository.delete(userEntity);
    }
}


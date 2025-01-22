package com.T1.CURD.Service;

import com.T1.CURD.Entity.UserEntity;
import com.T1.CURD.Repository.UserRepository;
import com.example.demo.dto.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        return toDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = toEntity(userDTO);
        UserEntity savedUser = userRepository.save(userEntity);
        return toDTO(savedUser);
    }

    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setAge(userDTO.getAge());

        UserEntity updatedUser = userRepository.save(existingUser);
        return toDTO(updatedUser);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
        userRepository.deleteById(id);
    }

    // DTO to Entity conversion
    private UserEntity toEntity(UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        return user;
    }

    // Entity to DTO conversion
    private UserDTO toDTO(UserEntity user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId().toString());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        return dto;
    }
}

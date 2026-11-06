package com.example.projectIntern.user.service;

import com.example.projectIntern.user.dto.UserRequestDTO;
import com.example.projectIntern.user.dto.UserResponseDTO;
import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllActiveUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO createUser(UserRequestDTO requestDTO);
    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);
    void deleteUser(Long id);
}
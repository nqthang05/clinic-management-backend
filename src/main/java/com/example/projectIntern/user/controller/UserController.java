package com.example.projectIntern.user.controller;

import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.user.dto.UserRequestDTO;
import com.example.projectIntern.user.dto.UserResponseDTO;
import com.example.projectIntern.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;


    @GetMapping
    public ApiResponse<List<UserResponseDTO>> getAllUsers() {
        return ApiResponse.<List<UserResponseDTO>>builder()
                .result(userService.getAllActiveUsers())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ApiResponse.<UserResponseDTO>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        return ApiResponse.<UserResponseDTO>builder()
                .result(userService.createUser(requestDTO))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO requestDTO) {
        return ApiResponse.<UserResponseDTO>builder()
                .result(userService.updateUser(id,requestDTO))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ApiResponse.<Void>builder()
                .message("User deleted successfully")
                .build();
    }
}
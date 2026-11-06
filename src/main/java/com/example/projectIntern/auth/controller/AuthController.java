package com.example.projectIntern.auth.controller;

import com.example.projectIntern.auth.dto.AuthRequestDTO;
import com.example.projectIntern.auth.dto.AuthResponseDTO;
import com.example.projectIntern.auth.service.AuthService;
import com.example.projectIntern.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        return ApiResponse.<AuthResponseDTO>builder()
                .result(authService.login(request))
                .build();
    }
}
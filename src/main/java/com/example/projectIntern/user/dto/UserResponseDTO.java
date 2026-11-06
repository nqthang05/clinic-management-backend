package com.example.projectIntern.user.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String roleName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
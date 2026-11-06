package com.example.projectIntern.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;

    @NotNull(message = "Role ID không được để trống")
    private Integer roleId; // Nhận Role ID từ Frontend
}
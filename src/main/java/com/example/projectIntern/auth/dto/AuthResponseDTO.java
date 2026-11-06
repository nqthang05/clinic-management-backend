package com.example.projectIntern.auth.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponseDTO {
    private String token;
    private String type; // Mặc định là "Bearer "
    private String username;
    private String role;
}
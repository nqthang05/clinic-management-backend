package com.example.projectIntern.patient.dto;

import com.example.projectIntern.common.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PatientResponseDTO {
    private Long id;
    private Long userId; // Sẽ null nếu không có acc
    private String fullName;
    private String phone;
    private LocalDate dob;
    private Gender gender;
    private String address;
    private String emergencyContact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
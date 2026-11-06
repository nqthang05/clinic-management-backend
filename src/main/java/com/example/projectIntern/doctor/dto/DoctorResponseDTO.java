package com.example.projectIntern.doctor.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class DoctorResponseDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String specialty;
    private Integer experienceYears;
    private BigDecimal consultationFee;
    private Boolean isActive;
}
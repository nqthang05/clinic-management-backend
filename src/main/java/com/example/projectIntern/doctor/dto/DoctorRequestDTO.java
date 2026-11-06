package com.example.projectIntern.doctor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class DoctorRequestDTO {
    @NotNull(message = "User ID không được để trống")
    private Long userId;

    private String specialty;

    private Integer experienceYears;

    @NotNull(message = "Phí khám không được để trống")
    private BigDecimal consultationFee;
}
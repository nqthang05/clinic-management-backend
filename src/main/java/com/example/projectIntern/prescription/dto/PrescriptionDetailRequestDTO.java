package com.example.projectIntern.prescription.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PrescriptionDetailRequestDTO {
    @NotNull
    private Long medicineId;
    @NotNull
    private Integer quantity;
    @NotNull
    private String dosage;
    @NotNull
    private Integer durationDays;
}
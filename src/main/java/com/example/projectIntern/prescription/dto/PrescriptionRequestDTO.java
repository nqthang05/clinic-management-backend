package com.example.projectIntern.prescription.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PrescriptionRequestDTO {
    @NotNull
    private Long medicalRecordId;
    private String instructions;
    private List<PrescriptionDetailRequestDTO> details; // Mảng chi tiết thuốc
}
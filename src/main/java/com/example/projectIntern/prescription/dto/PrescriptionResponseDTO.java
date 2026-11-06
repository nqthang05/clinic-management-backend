package com.example.projectIntern.prescription.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PrescriptionResponseDTO {
    private Long id;
    private Long medicalRecordId;
    private String instructions;
    private List<PrescriptionDetailResponseDTO> details;
}
package com.example.projectIntern.prescription.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
 public class PrescriptionDetailResponseDTO {
    private Long id;
    private Long medicineId;
    private String medicineName;
    private Integer quantity;
    private String dosage;
    private Integer durationDays;
    private BigDecimal unitPrice;
}
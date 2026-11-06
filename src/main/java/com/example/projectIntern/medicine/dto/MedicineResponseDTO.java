package com.example.projectIntern.medicine.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class MedicineResponseDTO {
    private Long id;
    private String name;
    private String activeIngredient;
    private String unit;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
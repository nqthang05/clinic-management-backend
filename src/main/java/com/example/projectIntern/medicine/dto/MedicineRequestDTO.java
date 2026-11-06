package com.example.projectIntern.medicine.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class MedicineRequestDTO {

    @NotBlank(message = "Tên thuốc không được để trống")
    private String name;

    private String activeIngredient;

    @NotBlank(message = "Đơn vị tính không được để trống")
    private String unit;

    @NotNull(message = "Giá thuốc không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá thuốc phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn không được âm")
    private Integer stockQuantity;
}
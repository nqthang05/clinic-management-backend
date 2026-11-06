// RequestDTO
package com.example.projectIntern.medicalrecord.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalRecordRequestDTO {
    @NotNull(message = "Appointment ID không được để trống")
    private Long appointmentId;

    @NotBlank(message = "Chẩn đoán không được để trống")
    private String diagnosis;

    private String notes;
}

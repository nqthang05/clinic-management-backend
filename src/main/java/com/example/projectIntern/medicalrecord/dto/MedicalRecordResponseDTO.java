
package com.example.projectIntern.medicalrecord.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalRecordResponseDTO {
    private Long id;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String diagnosis;
    private String notes;
}


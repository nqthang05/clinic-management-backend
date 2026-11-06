package com.example.projectIntern.appointment.dto;

import com.example.projectIntern.common.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentResponseDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;
    private String reason;
}
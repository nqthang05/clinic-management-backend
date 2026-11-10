package com.example.projectIntern.appointment.dto;

import com.example.projectIntern.common.enums.AppointmentStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AppointmentRequestDTO {
    @NotNull(message = "Patient ID không được để trống")
    private Long patientId;

    @NotNull(message = "Doctor ID không được để trống")
    private Long doctorId;

    @NotNull(message = "Ngày hẹn không được để trống")
    @FutureOrPresent(message = "Ngày hẹn khám không được nằm trong quá khứ")
    private LocalDate appointmentDate;

    @NotNull(message = "Giờ hẹn không được để trống")
    private LocalTime appointmentTime;

    private String reason;
    private AppointmentStatus status; // Có thể truyền để update
}
package com.example.projectIntern.appointment.service;
import com.example.projectIntern.appointment.dto.*;
import com.example.projectIntern.common.response.PageResponse;

import java.util.List;

public interface AppointmentService {
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(Long id);
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO);
    PageResponse<AppointmentResponseDTO> getAppointmentsPaging(int page, int size);

    List<AppointmentResponseDTO> searchAppointments(String keyword);
}
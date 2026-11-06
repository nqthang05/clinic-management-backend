package com.example.projectIntern.appointment.controller;

import com.example.projectIntern.appointment.dto.*;
import com.example.projectIntern.appointment.service.AppointmentService;
import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService service;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<AppointmentResponseDTO>> getAll() {
        return ApiResponse.<List<AppointmentResponseDTO>>builder().result(service.getAllAppointments()).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<AppointmentResponseDTO> getById(@PathVariable Long id) {
        return ApiResponse.<AppointmentResponseDTO>builder().result(service.getAppointmentById(id)).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<AppointmentResponseDTO> create(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return ApiResponse.<AppointmentResponseDTO>builder().result(service.createAppointment(requestDTO)).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<AppointmentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        return ApiResponse.<AppointmentResponseDTO>builder().result(service.updateAppointment(id, requestDTO)).build();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PageResponse<AppointmentResponseDTO>> getAppointmentsPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<AppointmentResponseDTO>>builder()
                .result(service.getAppointmentsPaging(page, size))
                .build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<AppointmentResponseDTO>> searchAppointments(@RequestParam String keyword) {
        return ApiResponse.<List<AppointmentResponseDTO>>builder()
                .result(service.searchAppointments(keyword))
                .build();
    }
}
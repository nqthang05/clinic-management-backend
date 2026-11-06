package com.example.projectIntern.prescription.controller;

import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.prescription.dto.PrescriptionRequestDTO;
import com.example.projectIntern.prescription.dto.PrescriptionResponseDTO;
import com.example.projectIntern.prescription.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.projectIntern.common.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService service;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PrescriptionResponseDTO> getById(@PathVariable Long id) {
        return ApiResponse.<PrescriptionResponseDTO>builder().result(service.getPrescriptionById(id)).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ApiResponse<PrescriptionResponseDTO> create(@Valid @RequestBody PrescriptionRequestDTO requestDTO) {
        return ApiResponse.<PrescriptionResponseDTO>builder().result(service.createPrescription(requestDTO)).build();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PageResponse<PrescriptionResponseDTO>> getPrescriptionsPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<PrescriptionResponseDTO>>builder()
                .result(service.getPrescriptionsPaging(page, size))
                .build();
    }
}
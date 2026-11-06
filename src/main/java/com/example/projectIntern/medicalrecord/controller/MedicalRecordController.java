package com.example.projectIntern.medicalrecord.controller;

import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.medicalrecord.dto.MedicalRecordRequestDTO;
import com.example.projectIntern.medicalrecord.dto.MedicalRecordResponseDTO;
import com.example.projectIntern.medicalrecord.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService service;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<MedicalRecordResponseDTO>> getAll() {
        return ApiResponse.<List<MedicalRecordResponseDTO>>builder().result(service.getAll()).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<MedicalRecordResponseDTO> getById(@PathVariable Long id) {
        return ApiResponse.<MedicalRecordResponseDTO>builder().result(service.getById(id)).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ApiResponse<MedicalRecordResponseDTO> create(@Valid @RequestBody MedicalRecordRequestDTO requestDTO) {
        return ApiResponse.<MedicalRecordResponseDTO>builder().result(service.create(requestDTO)).build();
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR')")
    @PutMapping("/{id}")
    public ApiResponse<MedicalRecordResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequestDTO requestDTO) {
        return ApiResponse.<MedicalRecordResponseDTO>builder().result(service.update(id, requestDTO)).build();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PageResponse<MedicalRecordResponseDTO>> getMedicalRecordsPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<MedicalRecordResponseDTO>>builder()
                .result(service.getMedicalRecordsPaging(page, size))
                .build();
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<MedicalRecordResponseDTO>> searchMedicalRecords(@RequestParam String keyword) {
        return ApiResponse.<List<MedicalRecordResponseDTO>>builder()
                .result(service.searchMedicalRecords(keyword))
                .build();
    }
}
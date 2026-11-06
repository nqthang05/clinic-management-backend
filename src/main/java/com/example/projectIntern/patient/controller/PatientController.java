package com.example.projectIntern.patient.controller;

import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.patient.dto.PatientRequestDTO;
import com.example.projectIntern.patient.dto.PatientResponseDTO;
import com.example.projectIntern.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<PatientResponseDTO>> getAllPatients() {
        return ApiResponse.<List<PatientResponseDTO>>builder().result(patientService.getAllPatients()).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ApiResponse.<PatientResponseDTO>builder().result(patientService.getPatientById(id)).build();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO requestDTO) {
        return ApiResponse.<PatientResponseDTO>builder().result(patientService.createPatient(requestDTO)).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PatientResponseDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequestDTO requestDTO) {
        return ApiResponse.<PatientResponseDTO>builder().result(patientService.updatePatient(id, requestDTO)).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ApiResponse.<String>builder().result("Xóa hồ sơ bệnh nhân thành công").build();
    }


    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PageResponse<PatientResponseDTO>> getPatientsPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<PatientResponseDTO>>builder()
                .result(patientService.getPatientsPaging(page, size))
                .build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<PatientResponseDTO>> searchPatients(@RequestParam String keyword) {
        return ApiResponse.<List<PatientResponseDTO>>builder()
                .result(patientService.searchPatients(keyword))
                .build();
    }
}
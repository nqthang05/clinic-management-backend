package com.example.projectIntern.doctor.controller;

import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.doctor.dto.DoctorRequestDTO;
import com.example.projectIntern.doctor.dto.DoctorResponseDTO;
import com.example.projectIntern.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<DoctorResponseDTO>> getAllDoctors() {
        return ApiResponse.<List<DoctorResponseDTO>>builder().result(doctorService.getAllActiveDoctors()).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        return ApiResponse.<DoctorResponseDTO>builder().result(doctorService.getDoctorById(id)).build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorRequestDTO requestDTO) {
        return ApiResponse.<DoctorResponseDTO>builder().result(doctorService.createDoctor(requestDTO)).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<DoctorResponseDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRequestDTO requestDTO) {
        return ApiResponse.<DoctorResponseDTO>builder().result(doctorService.updateDoctor(id, requestDTO)).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ApiResponse.<String>builder().result("Xóa bác sĩ thành công").build();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PageResponse<DoctorResponseDTO>> getDoctorsPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<DoctorResponseDTO>>builder()
                .result(doctorService.getDoctorsPaging(page, size))
                .build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<DoctorResponseDTO>> searchDoctors(@RequestParam String keyword) {
        return ApiResponse.<List<DoctorResponseDTO>>builder()
                .result(doctorService.searchDoctors(keyword))
                .build();
    }
}
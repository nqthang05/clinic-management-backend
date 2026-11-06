package com.example.projectIntern.medicine.controller;

import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.medicine.dto.MedicineRequestDTO;
import com.example.projectIntern.medicine.dto.MedicineResponseDTO;
import com.example.projectIntern.medicine.service.MedicineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<List<MedicineResponseDTO>> getAllMedicines() {
        return ApiResponse.<List<MedicineResponseDTO>>builder().result(medicineService.getAllActiveMedicines()).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<MedicineResponseDTO> getMedicineById(@PathVariable Long id) {
        return ApiResponse.<MedicineResponseDTO>builder().result(medicineService.getMedicineById(id)).build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<MedicineResponseDTO> createMedicine(@Valid @RequestBody MedicineRequestDTO requestDTO) {
        return ApiResponse.<MedicineResponseDTO>builder().result(medicineService.createMedicine(requestDTO)).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<MedicineResponseDTO> updateMedicine(@PathVariable Long id, @Valid @RequestBody MedicineRequestDTO requestDTO) {
        return ApiResponse.<MedicineResponseDTO>builder().result(medicineService.updateMedicine(id, requestDTO)).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<String> deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return ApiResponse.<String>builder().result("Xóa thuốc thành công").build();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PageResponse<MedicineResponseDTO>> getMedicinesPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<MedicineResponseDTO>>builder()
                .result(medicineService.getMedicinesPaging(page, size))
                .build();
    }
}
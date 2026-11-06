package com.example.projectIntern.medicine.service;

import com.example.projectIntern.common.response.PageResponse;
import com.example.projectIntern.medicine.dto.MedicineRequestDTO;
import com.example.projectIntern.medicine.dto.MedicineResponseDTO;
import java.util.List;

public interface MedicineService {
    List<MedicineResponseDTO> getAllActiveMedicines();
    MedicineResponseDTO getMedicineById(Long id);
    MedicineResponseDTO createMedicine(MedicineRequestDTO requestDTO);
    MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO requestDTO);
    void deleteMedicine(Long id); // Xóa mềm

    PageResponse<MedicineResponseDTO> getMedicinesPaging(int page, int size);
}
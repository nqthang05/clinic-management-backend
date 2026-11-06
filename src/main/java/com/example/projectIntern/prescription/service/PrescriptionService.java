package com.example.projectIntern.prescription.service;

import com.example.projectIntern.prescription.dto.PrescriptionRequestDTO;
import com.example.projectIntern.prescription.dto.PrescriptionResponseDTO;
import com.example.projectIntern.common.response.PageResponse;
public interface PrescriptionService {
    PrescriptionResponseDTO getPrescriptionById(Long id);
    PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO requestDTO);
    PageResponse<PrescriptionResponseDTO> getPrescriptionsPaging(int page, int size);
}
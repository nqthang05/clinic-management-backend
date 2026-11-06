package com.example.projectIntern.medicalrecord.service;

import com.example.projectIntern.medicalrecord.dto.MedicalRecordRequestDTO;
import com.example.projectIntern.medicalrecord.dto.MedicalRecordResponseDTO;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;

public interface MedicalRecordService {
    List<MedicalRecordResponseDTO> getAll();
    MedicalRecordResponseDTO getById(Long id);
    MedicalRecordResponseDTO create(MedicalRecordRequestDTO requestDTO);
    MedicalRecordResponseDTO update(Long id, MedicalRecordRequestDTO requestDTO);

    PageResponse<MedicalRecordResponseDTO> getMedicalRecordsPaging(int page, int size);

    List<MedicalRecordResponseDTO> searchMedicalRecords(String keyword);
}
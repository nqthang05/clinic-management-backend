package com.example.projectIntern.patient.service;

import com.example.projectIntern.patient.dto.PatientRequestDTO;
import com.example.projectIntern.patient.dto.PatientResponseDTO;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;

public interface PatientService {
    List<PatientResponseDTO> getAllPatients();
    PatientResponseDTO getPatientById(Long id);
    PatientResponseDTO createPatient(PatientRequestDTO requestDTO);
    PatientResponseDTO updatePatient(Long id, PatientRequestDTO requestDTO);
    void deletePatient(Long id); // Xóa cứng vì không có nghiệp vụ is_active

    PageResponse<PatientResponseDTO> getPatientsPaging(int page, int size);

    List<PatientResponseDTO> searchPatients(String keyword);
}
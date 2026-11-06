package com.example.projectIntern.doctor.service;

import com.example.projectIntern.doctor.dto.DoctorRequestDTO;
import com.example.projectIntern.doctor.dto.DoctorResponseDTO;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;
public interface DoctorService {
    List<DoctorResponseDTO> getAllActiveDoctors();
    DoctorResponseDTO getDoctorById(Long id);
    DoctorResponseDTO createDoctor(DoctorRequestDTO requestDTO);
    DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO requestDTO);
    void deleteDoctor(Long id);

    PageResponse<DoctorResponseDTO> getDoctorsPaging(int page, int size);

    List<DoctorResponseDTO> searchDoctors(String keyword);
}
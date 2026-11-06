package com.example.projectIntern.patient.service;

import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.patient.dto.PatientRequestDTO;
import com.example.projectIntern.patient.dto.PatientResponseDTO;
import com.example.projectIntern.patient.entity.Patient;
import com.example.projectIntern.patient.mapper.PatientMapper;
import com.example.projectIntern.patient.repository.PatientRepository;
import com.example.projectIntern.user.entity.User;
import com.example.projectIntern.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.projectIntern.common.response.PageResponse;
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PatientMapper patientMapper;

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        return patientMapper.toResponseDTO(patient);
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO requestDTO) {
        Patient patient = patientMapper.toEntity(requestDTO);
        if (requestDTO.getUserId() != null) {
            if (patientRepository.existsByUserId(requestDTO.getUserId())) throw new AppException(ErrorCode.USER_ALREADY_HAS_PATIENT_PROFILE);
            User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            patient.setUser(user);
        }
        return patientMapper.toResponseDTO(patientRepository.save(patient));
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        patientMapper.updateEntityFromDTO(requestDTO, patient);
        return patientMapper.toResponseDTO(patientRepository.save(patient));
    }

    @Override
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) throw new AppException(ErrorCode.PATIENT_NOT_FOUND);
        patientRepository.deleteById(id);
    }
    @Override
    public PageResponse<PatientResponseDTO> getPatientsPaging(int page, int size) {
        int pageNo = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());

        Page<Patient> patientPage = patientRepository.findAll(pageable);

        List<PatientResponseDTO> dtoList = patientPage.getContent().stream()
                .map(patientMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PageResponse.<PatientResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(patientPage.getTotalPages())
                .totalElements(patientPage.getTotalElements())
                .data(dtoList)
                .build();
    }

    @Override
    public List<PatientResponseDTO> searchPatients(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return List.of();
        return patientRepository.searchPatients(keyword.trim()).stream()
                .map(patientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
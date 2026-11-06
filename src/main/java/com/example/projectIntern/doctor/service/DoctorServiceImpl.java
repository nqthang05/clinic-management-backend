package com.example.projectIntern.doctor.service;

import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.doctor.dto.DoctorRequestDTO;
import com.example.projectIntern.doctor.dto.DoctorResponseDTO;
import com.example.projectIntern.doctor.entity.Doctor;
import com.example.projectIntern.doctor.mapper.DoctorMapper;
import com.example.projectIntern.doctor.repository.DoctorRepository;
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
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorResponseDTO> getAllActiveDoctors() {
        return doctorRepository.findByIsActiveTrue().stream().map(doctorMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DOCTOR_NOT_FOUND));
        return doctorMapper.toResponseDTO(doctor);
    }

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO requestDTO) {
        if (doctorRepository.existsByUserId(requestDTO.getUserId())) throw new AppException(ErrorCode.USER_ALREADY_HAS_DOCTOR_PROFILE);
        User user = userRepository.findById(requestDTO.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!user.getRole().getName().equals("ROLE_DOCTOR")) throw new AppException(ErrorCode.USER_NOT_DOCTOR_ROLE);

        Doctor doctor = doctorMapper.toEntity(requestDTO);
        doctor.setUser(user);
        doctor.setIsActive(true);
        return doctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO requestDTO) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DOCTOR_NOT_FOUND));
        doctorMapper.updateEntityFromDTO(requestDTO, doctor);
        return doctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DOCTOR_NOT_FOUND));
        doctor.setIsActive(false);
        doctorRepository.save(doctor);
    }

    @Override
    public PageResponse<DoctorResponseDTO> getDoctorsPaging(int page, int size) {
        int pageNo = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("experienceYears").descending()); // Ưu tiên xếp bác sĩ nhiều kinh nghiệm lên trước

        Page<Doctor> doctorPage = doctorRepository.findByIsActiveTrue(pageable);

        List<DoctorResponseDTO> dtoList = doctorPage.getContent().stream()
                .map(doctorMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PageResponse.<DoctorResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorPage.getTotalPages())
                .totalElements(doctorPage.getTotalElements())
                .data(dtoList)
                .build();
    }

    @Override
    public List<DoctorResponseDTO> searchDoctors(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return List.of();
        return doctorRepository.searchDoctors(keyword.trim()).stream()
                .map(doctorMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
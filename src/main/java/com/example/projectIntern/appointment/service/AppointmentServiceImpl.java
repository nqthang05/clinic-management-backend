package com.example.projectIntern.appointment.service;

import com.example.projectIntern.appointment.dto.*;
import com.example.projectIntern.appointment.entity.Appointment;
import com.example.projectIntern.appointment.mapper.AppointmentMapper;
import com.example.projectIntern.appointment.repository.AppointmentRepository;
import com.example.projectIntern.common.enums.AppointmentStatus;
import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.doctor.entity.Doctor;
import com.example.projectIntern.doctor.repository.DoctorRepository;
import com.example.projectIntern.patient.entity.Patient;
import com.example.projectIntern.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.projectIntern.common.response.PageResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.projectIntern.user.entity.User;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointmentMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment app = appointmentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        return appointmentMapper.toResponseDTO(app);
    }

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO) {
        Patient patient = patientRepository.findById(requestDTO.getPatientId()).orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId()).orElseThrow(() -> new AppException(ErrorCode.DOCTOR_NOT_FOUND));

        Appointment appointment = appointmentMapper.toEntity(requestDTO);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        if (requestDTO.getStatus() == null) appointment.setStatus(AppointmentStatus.PENDING);

        return appointmentMapper.toResponseDTO(appointmentRepository.save(appointment));
    }

    // --- Hàm nội bộ để lấy User đang đăng nhập ---
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        User currentUser = getCurrentUser();

        // CHẶN QUYỀN (Nếu là bác sĩ, nhưng lịch hẹn này không phải của bác sĩ đó -> Báo lỗi 403)
        if (currentUser.getRole().getName().equals("ROLE_DOCTOR") &&
                !appointment.getDoctor().getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        // Logic update giữ nguyên...
        if (!appointment.getPatient().getId().equals(requestDTO.getPatientId())) {
            Patient p = patientRepository.findById(requestDTO.getPatientId()).orElseThrow();
            appointment.setPatient(p);
        }
        if (!appointment.getDoctor().getId().equals(requestDTO.getDoctorId())) {
            Doctor d = doctorRepository.findById(requestDTO.getDoctorId()).orElseThrow();
            appointment.setDoctor(d);
        }

        appointmentMapper.updateEntityFromDTO(requestDTO, appointment);
        return appointmentMapper.toResponseDTO(appointmentRepository.save(appointment));
    }

    // 1. Phân trang: Bác sĩ chỉ thấy lịch của mình, Admin/Lễ tân thấy tất cả
    @Override
    public PageResponse<AppointmentResponseDTO> getAppointmentsPaging(int page, int size) {
        int pageNo = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());

        User currentUser = getCurrentUser();
        Page<Appointment> appointmentPage;

        // Nếu là Bác sĩ -> Lọc theo ID của Bác sĩ
        if (currentUser.getRole().getName().equals("ROLE_DOCTOR")) {
            appointmentPage = appointmentRepository.findByDoctorUserId(currentUser.getId(), pageable);
        } else {
            // Admin hoặc Lễ tân -> Lấy tất cả
            appointmentPage = appointmentRepository.findAll(pageable);
        }

        List<AppointmentResponseDTO> dtoList = appointmentPage.getContent().stream()
                .map(appointmentMapper::toResponseDTO).collect(Collectors.toList());

        return PageResponse.<AppointmentResponseDTO>builder()
                .currentPage(page).pageSize(size)
                .totalPages(appointmentPage.getTotalPages())
                .totalElements(appointmentPage.getTotalElements())
                .data(dtoList).build();
    }

    @Override
    public List<AppointmentResponseDTO> searchAppointments(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        List<Appointment> appointments = appointmentRepository.searchAppointments(keyword.trim());

        User currentUser = getCurrentUser();
        // Bác sĩ chỉ tìm thấy lịch hẹn của mình
        if (currentUser.getRole().getName().equals("ROLE_DOCTOR")) {
            appointments = appointments.stream()
                    .filter(a -> a.getDoctor().getUser().getId().equals(currentUser.getId()))
                    .collect(Collectors.toList());
        }

        return appointments.stream()
                .map(appointmentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
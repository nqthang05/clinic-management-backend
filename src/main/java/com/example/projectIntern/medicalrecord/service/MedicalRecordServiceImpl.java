package com.example.projectIntern.medicalrecord.service;

import com.example.projectIntern.appointment.entity.Appointment;
import com.example.projectIntern.appointment.repository.AppointmentRepository;
import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.medicalrecord.dto.MedicalRecordRequestDTO;
import com.example.projectIntern.medicalrecord.dto.MedicalRecordResponseDTO;
import com.example.projectIntern.medicalrecord.entity.MedicalRecord;
import com.example.projectIntern.medicalrecord.mapper.MedicalRecordMapper;
import com.example.projectIntern.medicalrecord.repository.MedicalRecordRepository;
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
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public List<MedicalRecordResponseDTO> getAll() {
        return medicalRecordRepository.findAll().stream().map(medicalRecordMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public MedicalRecordResponseDTO getById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MEDICAL_RECORD_NOT_FOUND));
        return medicalRecordMapper.toResponseDTO(record);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // 2. Chặn lúc Tạo (Create)
    @Override
    public MedicalRecordResponseDTO create(MedicalRecordRequestDTO requestDTO) {
        if (medicalRecordRepository.existsByAppointmentId(requestDTO.getAppointmentId())) {
            throw new AppException(ErrorCode.MEDICAL_RECORD_EXISTED);
        }
        Appointment app = appointmentRepository.findById(requestDTO.getAppointmentId())
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // =======================================================
        // 1. RÀNG BUỘC: Lịch hẹn phải CONFIRMED mới được lập Bệnh án
        // =======================================================
        if (!"CONFIRMED".equals(app.getStatus().name())) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_CONFIRMED);
        }

        User currentUser = getCurrentUser();
        if (currentUser.getRole().getName().equals("ROLE_DOCTOR") &&
                !app.getDoctor().getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        MedicalRecord record = medicalRecordMapper.toEntity(requestDTO);
        record.setAppointment(app);
        record.setPatient(app.getPatient());
        record.setDoctor(app.getDoctor());

        MedicalRecord savedRecord = medicalRecordRepository.save(record);

        // =======================================================
        // 2. TỰ ĐỘNG CHUYỂN TRẠNG THÁI: Lập bệnh án xong -> Lịch hẹn COMPLETED
        // =======================================================
        app.setStatus(com.example.projectIntern.common.enums.AppointmentStatus.COMPLETED);
        appointmentRepository.save(app);

        return medicalRecordMapper.toResponseDTO(savedRecord);
    }

    @Override
    public MedicalRecordResponseDTO update(Long id, MedicalRecordRequestDTO requestDTO) {
        MedicalRecord record = medicalRecordRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MEDICAL_RECORD_NOT_FOUND));
        medicalRecordMapper.updateEntityFromDTO(requestDTO, record);
        return medicalRecordMapper.toResponseDTO(medicalRecordRepository.save(record));
    }

    // 1. Phân trang
    @Override
    public PageResponse<MedicalRecordResponseDTO> getMedicalRecordsPaging(int page, int size) {
        int pageNo = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());

        User currentUser = getCurrentUser();
        Page<MedicalRecord> recordPage;

        if (currentUser.getRole().getName().equals("ROLE_DOCTOR")) {
            recordPage = medicalRecordRepository.findByDoctorUserId(currentUser.getId(), pageable);
        } else {
            recordPage = medicalRecordRepository.findAll(pageable);
        }

        List<MedicalRecordResponseDTO> dtoList = recordPage.getContent().stream()
                .map(medicalRecordMapper::toResponseDTO).collect(Collectors.toList());

        return PageResponse.<MedicalRecordResponseDTO>builder()
                .currentPage(page).pageSize(size)
                .totalPages(recordPage.getTotalPages())
                .totalElements(recordPage.getTotalElements())
                .data(dtoList).build();
    }

    @Override
    public List<MedicalRecordResponseDTO> searchMedicalRecords(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of(); // Trả về mảng rỗng nếu không nhập gì
        }

        // 1. Lấy danh sách thô từ Database
        List<MedicalRecord> records = medicalRecordRepository.searchMedicalRecords(keyword.trim());

        // 2. Lọc bảo mật (Data-Level Security)
        User currentUser = getCurrentUser();
        if (currentUser.getRole().getName().equals("ROLE_DOCTOR")) {
            records = records.stream()
                    .filter(r -> r.getDoctor().getUser().getId().equals(currentUser.getId()))
                    .collect(Collectors.toList());
        }

        // 3. Map sang DTO trả về Frontend
        return records.stream()
                .map(medicalRecordMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
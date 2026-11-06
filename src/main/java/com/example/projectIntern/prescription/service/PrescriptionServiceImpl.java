package com.example.projectIntern.prescription.service;

import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.medicalrecord.entity.MedicalRecord;
import com.example.projectIntern.medicalrecord.repository.MedicalRecordRepository;
import com.example.projectIntern.medicine.entity.Medicine;
import com.example.projectIntern.medicine.repository.MedicineRepository;
import com.example.projectIntern.prescription.dto.PrescriptionDetailRequestDTO;
import com.example.projectIntern.prescription.dto.PrescriptionRequestDTO;
import com.example.projectIntern.prescription.dto.PrescriptionResponseDTO;
import com.example.projectIntern.prescription.entity.Prescription;
import com.example.projectIntern.prescription.entity.PrescriptionDetail;
import com.example.projectIntern.prescription.mapper.PrescriptionMapper;
import com.example.projectIntern.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.projectIntern.common.response.PageResponse;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import com.example.projectIntern.user.entity.User;
@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    public PrescriptionResponseDTO getPrescriptionById(Long id) {
        Prescription p = prescriptionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRESCRIPTION_NOT_FOUND));
        return prescriptionMapper.toResponseDTO(p);
    }
    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    @Override
    @Transactional
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO requestDTO) {
        if(prescriptionRepository.existsByMedicalRecordId(requestDTO.getMedicalRecordId())) {
            throw new AppException(ErrorCode.PRESCRIPTION_EXISTED);
        }

        MedicalRecord record = medicalRecordRepository.findById(requestDTO.getMedicalRecordId())
                .orElseThrow(() -> new AppException(ErrorCode.MEDICAL_RECORD_NOT_FOUND));

        User currentUser = getCurrentUser();

        // Nếu người dùng hiện tại là BÁC SĨ, NHƯNG id của bác sĩ trong bệnh án KHÁC với id của người dùng hiện tại
        if (currentUser.getRole().getName().equals("ROLE_DOCTOR") &&
                !record.getDoctor().getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN); // Ném lỗi 403 Access Denied
        }
        // =========================================================================

        Prescription prescription = new Prescription();
        prescription.setMedicalRecord(record);
        prescription.setInstructions(requestDTO.getInstructions());

        List<PrescriptionDetail> details = new ArrayList<>();

        for (PrescriptionDetailRequestDTO item : requestDTO.getDetails()) {
            Medicine medicine = medicineRepository.findById(item.getMedicineId())
                    .orElseThrow(() -> new AppException(ErrorCode.MEDICINE_NOT_FOUND));

            if (medicine.getStockQuantity() < item.getQuantity()) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }
            medicine.setStockQuantity(medicine.getStockQuantity() - item.getQuantity());
            medicineRepository.save(medicine);

            PrescriptionDetail detail = new PrescriptionDetail();
            detail.setPrescription(prescription);
            detail.setMedicine(medicine);
            detail.setQuantity(item.getQuantity());
            detail.setDosage(item.getDosage());
            detail.setDurationDays(item.getDurationDays());
            detail.setUnitPrice(medicine.getPrice()); // Chốt giá thuốc tại thời điểm kê đơn

            details.add(detail);
        }

        prescription.setDetails(details);
        return prescriptionMapper.toResponseDTO(prescriptionRepository.save(prescription));
    }



    // 1. Phân trang
    @Override
    public PageResponse<PrescriptionResponseDTO> getPrescriptionsPaging(int page, int size) {
        int pageNo = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());

        User currentUser = getCurrentUser();
        Page<Prescription> prescriptionPage;

        if (currentUser.getRole().getName().equals("ROLE_DOCTOR")) {
            // Dùng hàm repository vừa khai báo ở Bước 1
            prescriptionPage = prescriptionRepository.findByMedicalRecordDoctorUserId(currentUser.getId(), pageable);
        } else {
            prescriptionPage = prescriptionRepository.findAll(pageable);
        }

        List<PrescriptionResponseDTO> dtoList = prescriptionPage.getContent().stream()
                .map(prescriptionMapper::toResponseDTO).collect(Collectors.toList());

        return PageResponse.<PrescriptionResponseDTO>builder()
                .currentPage(page).pageSize(size)
                .totalPages(prescriptionPage.getTotalPages())
                .totalElements(prescriptionPage.getTotalElements())
                .data(dtoList).build();
    }
}
package com.example.projectIntern.prescription.repository;

import com.example.projectIntern.prescription.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByMedicalRecordId(Long medicalRecordId);
    boolean existsByMedicalRecordId(Long medicalRecordId);

    // Lấy đơn thuốc phân trang của RIÊNG Bác sĩ đó (Join qua bảng MedicalRecord)
    Page<Prescription> findByMedicalRecordDoctorUserId(Long userId, Pageable pageable);
}
package com.example.projectIntern.medicalrecord.repository;

import com.example.projectIntern.medicalrecord.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    boolean existsByAppointmentId(Long appointmentId);
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);

    // Lấy bệnh án phân trang của RIÊNG Bác sĩ đó
    Page<MedicalRecord> findByDoctorUserId(Long userId, Pageable pageable);

    @Query("SELECT m FROM MedicalRecord m WHERE LOWER(m.patient.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR m.patient.phone LIKE CONCAT('%', :keyword, '%')")
    List<MedicalRecord> searchMedicalRecords(@Param("keyword") String keyword);
}
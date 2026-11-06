package com.example.projectIntern.patient.repository;

import com.example.projectIntern.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByUserId(Long userId);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR p.phone LIKE CONCAT('%', :keyword, '%')")
    List<Patient> searchPatients(@Param("keyword") String keyword);
}
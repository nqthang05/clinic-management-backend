package com.example.projectIntern.doctor.repository;

import com.example.projectIntern.doctor.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByIsActiveTrue();
    boolean existsByUserId(Long userId);

    long countByIsActiveTrue();

    Page<Doctor> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE d.isActive = true AND LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Doctor> searchDoctors(@Param("keyword") String keyword);
}
package com.example.projectIntern.medicine.repository;

import com.example.projectIntern.medicine.entity.Medicine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    // Chỉ lấy các thuốc còn đang kinh doanh
    List<Medicine> findByIsActiveTrue();

    Page<Medicine> findByIsActiveTrue(Pageable pageable);
}
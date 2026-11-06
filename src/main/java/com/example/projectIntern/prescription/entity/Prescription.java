package com.example.projectIntern.prescription.entity;

import com.example.projectIntern.common.entity.BaseEntity;
import com.example.projectIntern.medicalrecord.entity.MedicalRecord;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
public class Prescription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "medical_record_id", nullable = false, unique = true)
    private MedicalRecord medicalRecord;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionDetail> details;
}


package com.example.projectIntern.prescription.entity;

import com.example.projectIntern.medicine.entity.Medicine;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "prescription_details")
@Getter
@Setter
public class PrescriptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    private Integer quantity;
    private String dosage;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
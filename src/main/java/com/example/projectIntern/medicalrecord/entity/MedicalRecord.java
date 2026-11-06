package com.example.projectIntern.medicalrecord.entity;

import com.example.projectIntern.appointment.entity.Appointment;
import com.example.projectIntern.common.entity.BaseEntity;
import com.example.projectIntern.doctor.entity.Doctor;
import com.example.projectIntern.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
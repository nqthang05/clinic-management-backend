package com.example.projectIntern.doctor.entity;

import com.example.projectIntern.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ 1-1: Mỗi bác sĩ ứng với 1 tài khoản
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 100)
    private String specialty;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "consultation_fee", nullable = false)
    private BigDecimal consultationFee;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
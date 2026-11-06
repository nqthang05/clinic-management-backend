package com.example.projectIntern.patient.entity;

import com.example.projectIntern.common.entity.BaseEntity;
import com.example.projectIntern.common.enums.Gender;
import com.example.projectIntern.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Có thể NULL (bệnh nhân đăng ký tại quầy không có tài khoản app)
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(length = 20)
    private String phone;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    @Column(name = "emergency_contact", length = 50)
    private String emergencyContact;
}
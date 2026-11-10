package com.example.projectIntern.patient.dto;

import com.example.projectIntern.common.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class PatientRequestDTO {
    // Để null nếu bệnh nhân đăng ký tại quầy không có tài khoản app
    private Long userId;

    @NotBlank(message = "Tên bệnh nhân không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @PastOrPresent(message = "Ngày sinh không được là ngày trong tương lai")
    private LocalDate dob;
    private Gender gender;
    private String address;
    private String emergencyContact;
}
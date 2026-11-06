package com.example.projectIntern.dashboard.service;

import com.example.projectIntern.appointment.repository.AppointmentRepository;
import com.example.projectIntern.dashboard.dto.DashboardOverviewDTO;
import com.example.projectIntern.doctor.repository.DoctorRepository;
import com.example.projectIntern.invoice.repository.InvoiceRepository;
import com.example.projectIntern.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardOverviewDTO getOverviewMetrics() {
        LocalDate today = LocalDate.now();

        // Lấy doanh thu tháng này
        BigDecimal monthlyRevenue = invoiceRepository.sumRevenueByMonthAndYear(today.getMonthValue(), today.getYear());
        // Xử lý null nếu tháng này chưa có ai thanh toán
        if (monthlyRevenue == null) {
            monthlyRevenue = BigDecimal.ZERO;
        }

        return DashboardOverviewDTO.builder()
                .totalPatients(patientRepository.count())
                .totalActiveDoctors(doctorRepository.countByIsActiveTrue())
                .todayAppointments(appointmentRepository.countByAppointmentDate(today))
                .currentMonthRevenue(monthlyRevenue)
                .build();
    }
}
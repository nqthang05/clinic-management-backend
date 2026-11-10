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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;

    public DashboardOverviewDTO getOverviewMetrics() {
        LocalDate today = LocalDate.now();

        // 1. Chỉ số tổng quan
        BigDecimal monthlyRevenue = invoiceRepository.sumRevenueByMonthAndYear(today.getMonthValue(), today.getYear());
        if (monthlyRevenue == null) {
            monthlyRevenue = BigDecimal.ZERO;
        }
        long totalDoctors = doctorRepository.countByIsActiveTrue();

        // 2. Dữ liệu biểu đồ: Cơ cấu trạng thái Lịch Hẹn
        List<DashboardOverviewDTO.StatusData> statusDataList = appointmentRepository.countAppointmentsByStatus().stream()
                .map(obj -> DashboardOverviewDTO.StatusData.builder()
                        .name(obj[0].toString())
                        .value((Long) obj[1])
                        .build())
                .collect(Collectors.toList());

        // 3. Dữ liệu biểu đồ: Cơ cấu Bác sĩ theo Chuyên Khoa
        List<DashboardOverviewDTO.SpecialtyData> specialtyDataList = doctorRepository.countActiveDoctorsBySpecialty().stream()
                .map(obj -> {
                    String specialty = obj[0].toString();
                    long count = (Long) obj[1];
                    // Tính phần trăm làm tròn
                    int percent = totalDoctors == 0 ? 0 : (int) Math.round((double) count / totalDoctors * 100);
                    return DashboardOverviewDTO.SpecialtyData.builder()
                            .name(specialty)
                            .count(count)
                            .percent(percent)
                            .build();
                })
                .collect(Collectors.toList());

        // 4. Dữ liệu biểu đồ: Xu hướng lịch hẹn (7 ngày qua)
        LocalDate sevenDaysAgo = today.minusDays(6);
        List<Object[]> rawTrend = appointmentRepository.countAppointmentsByDateFrom(sevenDaysAgo);

        // Chuyển kết quả truy vấn thành Map để dễ tra cứu
        Map<LocalDate, Long> trendMap = rawTrend.stream()
                .collect(Collectors.toMap(obj -> (LocalDate) obj[0], obj -> (Long) obj[1]));

        List<DashboardOverviewDTO.TrendData> trendDataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        // Lặp qua 7 ngày (từ 6 ngày trước đến hôm nay), nếu ngày nào không có lịch hẹn thì gán count = 0
        for (int i = 0; i <= 6; i++) {
            LocalDate date = sevenDaysAgo.plusDays(i);
            trendDataList.add(DashboardOverviewDTO.TrendData.builder()
                    .date(date.format(formatter))
                    .count(trendMap.getOrDefault(date, 0L)) // Lấy count từ Map, không có thì trả về 0
                    .build());
        }

        return DashboardOverviewDTO.builder()
                .totalPatients(patientRepository.count())
                .totalActiveDoctors(totalDoctors)
                .todayAppointments(appointmentRepository.countByAppointmentDate(today))
                .currentMonthRevenue(monthlyRevenue)
                .statusData(statusDataList)
                .trendData(trendDataList)
                .specialtyData(specialtyDataList)
                .build();
    }
}
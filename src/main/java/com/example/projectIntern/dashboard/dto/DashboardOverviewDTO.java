package com.example.projectIntern.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DashboardOverviewDTO {
    private long totalPatients;
    private long totalActiveDoctors;
    private long todayAppointments;
    private BigDecimal currentMonthRevenue;
}
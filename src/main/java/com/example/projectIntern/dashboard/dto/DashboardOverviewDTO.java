package com.example.projectIntern.dashboard.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class DashboardOverviewDTO {
    private long totalPatients;
    private long totalActiveDoctors;
    private long todayAppointments;
    private BigDecimal currentMonthRevenue;

    private List<StatusData> statusData;
    private List<TrendData> trendData;
    private List<SpecialtyData> specialtyData;

    @Getter @Setter @Builder
    public static class StatusData {
        private String name;
        private long value;
    }

    @Getter @Setter @Builder
    public static class TrendData {
        private String date;
        private long count;
    }

    @Getter @Setter @Builder
    public static class SpecialtyData {
        private String name;
        private long count;
        private int percent;
    }
}
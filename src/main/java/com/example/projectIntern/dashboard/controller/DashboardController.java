package com.example.projectIntern.dashboard.controller;

import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.dashboard.dto.DashboardOverviewDTO;
import com.example.projectIntern.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<DashboardOverviewDTO> getOverview() {
        return ApiResponse.<DashboardOverviewDTO>builder()
                .result(dashboardService.getOverviewMetrics())
                .build();
    }


}
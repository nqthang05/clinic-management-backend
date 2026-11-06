package com.example.projectIntern.invoice.controller;

import com.example.projectIntern.common.enums.PaymentMethod;
import com.example.projectIntern.common.response.ApiResponse;
import com.example.projectIntern.invoice.dto.InvoiceResponseDTO;
import com.example.projectIntern.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
public class InvoiceController {
    private final InvoiceService service;

    @GetMapping
    public ApiResponse<List<InvoiceResponseDTO>> getAll() {
        return ApiResponse.<List<InvoiceResponseDTO>>builder().result(service.getAllInvoices()).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<InvoiceResponseDTO> getById(@PathVariable Long id) {
        return ApiResponse.<InvoiceResponseDTO>builder().result(service.getInvoiceById(id)).build();
    }

    @PostMapping("/generate/{appointmentId}")
    public ApiResponse<InvoiceResponseDTO> generateInvoice(@PathVariable Long appointmentId) {
        return ApiResponse.<InvoiceResponseDTO>builder().result(service.generateInvoice(appointmentId)).build();
    }

    @PutMapping("/{id}/pay")
    public ApiResponse<InvoiceResponseDTO> payInvoice(@PathVariable Long id, @RequestParam PaymentMethod method) {
        return ApiResponse.<InvoiceResponseDTO>builder().result(service.payInvoice(id, method)).build();
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPTIONIST')")
    public ApiResponse<PageResponse<InvoiceResponseDTO>> getInvoicesPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<PageResponse<InvoiceResponseDTO>>builder()
                .result(service.getInvoicesPaging(page, size))
                .build();
    }
}
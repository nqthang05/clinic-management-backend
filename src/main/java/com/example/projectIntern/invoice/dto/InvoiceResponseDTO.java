package com.example.projectIntern.invoice.dto;

import com.example.projectIntern.common.enums.InvoiceStatus;
import com.example.projectIntern.common.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class InvoiceResponseDTO {
    private Long id;
    private Long appointmentId;
    private BigDecimal consultationFee;
    private BigDecimal medicineFee;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime paidAt;
}
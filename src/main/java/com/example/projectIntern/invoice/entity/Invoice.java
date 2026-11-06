package com.example.projectIntern.invoice.entity;

import com.example.projectIntern.appointment.entity.Appointment;
import com.example.projectIntern.common.entity.BaseEntity;
import com.example.projectIntern.common.enums.InvoiceStatus;
import com.example.projectIntern.common.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Invoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(name = "consultation_fee")
    private BigDecimal consultationFee;

    @Column(name = "medicine_fee")
    private BigDecimal medicineFee;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status = InvoiceStatus.UNPAID;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}
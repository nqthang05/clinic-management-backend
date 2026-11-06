package com.example.projectIntern.invoice.service;

import com.example.projectIntern.appointment.entity.Appointment;
import com.example.projectIntern.appointment.repository.AppointmentRepository;
import com.example.projectIntern.common.enums.InvoiceStatus;
import com.example.projectIntern.common.enums.PaymentMethod;
import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.invoice.dto.InvoiceResponseDTO;
import com.example.projectIntern.invoice.entity.Invoice;
import com.example.projectIntern.invoice.repository.InvoiceRepository;
import com.example.projectIntern.medicalrecord.entity.MedicalRecord;
import com.example.projectIntern.medicalrecord.repository.MedicalRecordRepository;
import com.example.projectIntern.prescription.entity.Prescription;
import com.example.projectIntern.prescription.entity.PrescriptionDetail;
import com.example.projectIntern.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.projectIntern.common.response.PageResponse;
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public List<InvoiceResponseDTO> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(this::mapToRes).collect(Collectors.toList());
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        return mapToRes(invoice);
    }

    @Override
    public InvoiceResponseDTO generateInvoice(Long appointmentId) {
        if (invoiceRepository.existsByAppointmentId(appointmentId)) throw new AppException(ErrorCode.INVOICE_EXISTED);
        Appointment app = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        BigDecimal consultationFee = app.getDoctor().getConsultationFee();
        BigDecimal medicineFee = BigDecimal.ZERO;

        MedicalRecord record = medicalRecordRepository.findByAppointmentId(appointmentId).orElse(null);
        if (record != null) {
            Prescription prescription = prescriptionRepository.findByMedicalRecordId(record.getId()).orElse(null);
            if (prescription != null) {
                for (PrescriptionDetail detail : prescription.getDetails()) {
                    BigDecimal detailTotal = detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity()));
                    medicineFee = medicineFee.add(detailTotal);
                }
            }
        }

        Invoice invoice = new Invoice();
        invoice.setAppointment(app);
        invoice.setConsultationFee(consultationFee);
        invoice.setMedicineFee(medicineFee);
        invoice.setTotalAmount(consultationFee.add(medicineFee));
        invoice.setStatus(InvoiceStatus.UNPAID);

        return mapToRes(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceResponseDTO payInvoice(Long invoiceId, PaymentMethod paymentMethod) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        if(invoice.getStatus() == InvoiceStatus.PAID) throw new AppException(ErrorCode.INVOICE_ALREADY_PAID);

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setPaidAt(LocalDateTime.now());

        return mapToRes(invoiceRepository.save(invoice));
    }

    private InvoiceResponseDTO mapToRes(Invoice inv) {
        InvoiceResponseDTO res = new InvoiceResponseDTO();
        res.setId(inv.getId());
        res.setAppointmentId(inv.getAppointment().getId());
        res.setConsultationFee(inv.getConsultationFee());
        res.setMedicineFee(inv.getMedicineFee());
        res.setTotalAmount(inv.getTotalAmount());
        res.setStatus(inv.getStatus());
        res.setPaymentMethod(inv.getPaymentMethod());
        res.setPaidAt(inv.getPaidAt());
        return res;
    }

    @Override
    public PageResponse<InvoiceResponseDTO> getInvoicesPaging(int page, int size) {
        int pageNo = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());

        Page<Invoice> invoicePage = invoiceRepository.findAll(pageable);

        List<InvoiceResponseDTO> dtoList = invoicePage.getContent().stream()
                .map(this::mapToRes) // Tái sử dụng hàm mapToRes sẵn có
                .collect(Collectors.toList());

        return PageResponse.<InvoiceResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(invoicePage.getTotalPages())
                .totalElements(invoicePage.getTotalElements())
                .data(dtoList)
                .build();
    }
}
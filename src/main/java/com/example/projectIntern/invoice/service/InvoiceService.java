package com.example.projectIntern.invoice.service;

import com.example.projectIntern.common.enums.PaymentMethod;
import com.example.projectIntern.invoice.dto.InvoiceResponseDTO;
import java.util.List;
import com.example.projectIntern.common.response.PageResponse;

public interface InvoiceService {
    List<InvoiceResponseDTO> getAllInvoices();
    InvoiceResponseDTO getInvoiceById(Long id);
    InvoiceResponseDTO generateInvoice(Long appointmentId);
    InvoiceResponseDTO payInvoice(Long invoiceId, PaymentMethod paymentMethod);

    PageResponse<InvoiceResponseDTO> getInvoicesPaging(int page, int size);
}
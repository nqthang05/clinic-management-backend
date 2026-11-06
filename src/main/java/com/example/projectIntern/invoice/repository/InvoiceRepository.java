package com.example.projectIntern.invoice.repository;

import com.example.projectIntern.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    boolean existsByAppointmentId(Long appointmentId);


    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PAID' AND MONTH(i.paidAt) = :month AND YEAR(i.paidAt) = :year")
    BigDecimal sumRevenueByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
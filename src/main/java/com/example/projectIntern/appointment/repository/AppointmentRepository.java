package com.example.projectIntern.appointment.repository;

import com.example.projectIntern.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    long countByAppointmentDate(LocalDate date);

//  Lấy lịch hẹn phân trang của RIÊNG Bác sĩ đó
    Page<Appointment> findByDoctorUserId(Long userId, Pageable pageable);


    @Query("SELECT a FROM Appointment a WHERE LOWER(a.patient.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR a.patient.phone LIKE CONCAT('%', :keyword, '%')")
    List<Appointment> searchAppointments(@Param("keyword") String keyword);

    // Đếm số lượng lịch hẹn theo từng trạng thái
    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    // Lấy số lượng lịch hẹn theo từng ngày (từ một mốc thời gian)
    @Query("SELECT a.appointmentDate, COUNT(a) FROM Appointment a WHERE a.appointmentDate >= :startDate GROUP BY a.appointmentDate ORDER BY a.appointmentDate ASC")
    List<Object[]> countAppointmentsByDateFrom(@Param("startDate") LocalDate startDate);
}
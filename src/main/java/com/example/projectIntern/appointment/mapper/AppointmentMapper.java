package com.example.projectIntern.appointment.mapper;

import com.example.projectIntern.appointment.dto.AppointmentRequestDTO;
import com.example.projectIntern.appointment.dto.AppointmentResponseDTO;
import com.example.projectIntern.appointment.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    Appointment toEntity(AppointmentRequestDTO requestDTO);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.fullName")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.user.fullName")
    AppointmentResponseDTO toResponseDTO(Appointment appointment);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    void updateEntityFromDTO(AppointmentRequestDTO requestDTO, @MappingTarget Appointment appointment);
}
package com.example.projectIntern.medicalrecord.mapper;

import com.example.projectIntern.medicalrecord.dto.MedicalRecordRequestDTO;
import com.example.projectIntern.medicalrecord.dto.MedicalRecordResponseDTO;
import com.example.projectIntern.medicalrecord.entity.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    MedicalRecord toEntity(MedicalRecordRequestDTO requestDTO);

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "patientName", source = "patient.fullName")
    @Mapping(target = "doctorName", source = "doctor.user.fullName")
    MedicalRecordResponseDTO toResponseDTO(MedicalRecord medicalRecord);

    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    void updateEntityFromDTO(MedicalRecordRequestDTO requestDTO, @MappingTarget MedicalRecord medicalRecord);
}
package com.example.projectIntern.patient.mapper;

import com.example.projectIntern.patient.dto.PatientRequestDTO;
import com.example.projectIntern.patient.dto.PatientResponseDTO;
import com.example.projectIntern.patient.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "user", ignore = true)
    Patient toEntity(PatientRequestDTO requestDTO);

    @Mapping(target = "userId", source = "user.id")
    PatientResponseDTO toResponseDTO(Patient patient);

    @Mapping(target = "user", ignore = true)
    void updateEntityFromDTO(PatientRequestDTO requestDTO, @MappingTarget Patient patient);
}
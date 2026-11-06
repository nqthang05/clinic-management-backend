package com.example.projectIntern.doctor.mapper;

import com.example.projectIntern.doctor.dto.DoctorRequestDTO;
import com.example.projectIntern.doctor.dto.DoctorResponseDTO;
import com.example.projectIntern.doctor.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "user", ignore = true)
    Doctor toEntity(DoctorRequestDTO requestDTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "fullName", source = "user.fullName")
    DoctorResponseDTO toResponseDTO(Doctor doctor);

    @Mapping(target = "user", ignore = true)
    void updateEntityFromDTO(DoctorRequestDTO requestDTO, @MappingTarget Doctor doctor);
}
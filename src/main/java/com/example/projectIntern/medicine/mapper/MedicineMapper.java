package com.example.projectIntern.medicine.mapper;

import com.example.projectIntern.medicine.dto.MedicineRequestDTO;
import com.example.projectIntern.medicine.dto.MedicineResponseDTO;
import com.example.projectIntern.medicine.entity.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicineMapper {
    Medicine toEntity(MedicineRequestDTO requestDTO);
    MedicineResponseDTO toResponseDTO(Medicine medicine);

    // Dùng cho API Update
    void updateEntityFromDTO(MedicineRequestDTO requestDTO, @MappingTarget Medicine medicine);
}
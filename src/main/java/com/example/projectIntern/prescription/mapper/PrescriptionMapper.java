package com.example.projectIntern.prescription.mapper;

import com.example.projectIntern.prescription.dto.PrescriptionDetailResponseDTO;
import com.example.projectIntern.prescription.dto.PrescriptionResponseDTO;
import com.example.projectIntern.prescription.entity.Prescription;
import com.example.projectIntern.prescription.entity.PrescriptionDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    // Map đối tượng cha
    @Mapping(target = "medicalRecordId", source = "medicalRecord.id")
    PrescriptionResponseDTO toResponseDTO(Prescription prescription);

    // Map đối tượng con (MapStruct tự động dùng hàm này cho List<PrescriptionDetail>)
    @Mapping(target = "medicineId", source = "medicine.id")
    @Mapping(target = "medicineName", source = "medicine.name")
    PrescriptionDetailResponseDTO toDetailResponseDTO(PrescriptionDetail detail);
}
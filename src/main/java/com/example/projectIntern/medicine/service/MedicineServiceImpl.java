package com.example.projectIntern.medicine.service;

import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.medicine.dto.MedicineRequestDTO;
import com.example.projectIntern.medicine.dto.MedicineResponseDTO;
import com.example.projectIntern.medicine.entity.Medicine;
import com.example.projectIntern.medicine.mapper.MedicineMapper;
import com.example.projectIntern.medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.projectIntern.common.response.PageResponse;
@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public List<MedicineResponseDTO> getAllActiveMedicines() {
        return medicineRepository.findByIsActiveTrue().stream().map(medicineMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public MedicineResponseDTO getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MEDICINE_NOT_FOUND));
        return medicineMapper.toResponseDTO(medicine);
    }

    @Override
    public MedicineResponseDTO createMedicine(MedicineRequestDTO requestDTO) {
        Medicine medicine = medicineMapper.toEntity(requestDTO);
        medicine.setIsActive(true);
        return medicineMapper.toResponseDTO(medicineRepository.save(medicine));
    }

    @Override
    public MedicineResponseDTO updateMedicine(Long id, MedicineRequestDTO requestDTO) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MEDICINE_NOT_FOUND));
        medicineMapper.updateEntityFromDTO(requestDTO, medicine);
        return medicineMapper.toResponseDTO(medicineRepository.save(medicine));
    }

    @Override
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MEDICINE_NOT_FOUND));
        medicine.setIsActive(false);
        medicineRepository.save(medicine);
    }

    @Override
    public PageResponse<MedicineResponseDTO> getMedicinesPaging(int page, int size) {
        int pageNo = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("createdAt").descending());

        Page<Medicine> medicinePage = medicineRepository.findByIsActiveTrue(pageable);

        List<MedicineResponseDTO> dtoList = medicinePage.getContent().stream()
                .map(medicineMapper::toResponseDTO)
                .collect(Collectors.toList());

        return PageResponse.<MedicineResponseDTO>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(medicinePage.getTotalPages())
                .totalElements(medicinePage.getTotalElements())
                .data(dtoList)
                .build();
    }
}
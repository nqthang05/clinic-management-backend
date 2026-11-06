package com.example.projectIntern.user.mapper;

import com.example.projectIntern.user.dto.UserRequestDTO;
import com.example.projectIntern.user.dto.UserResponseDTO;
import com.example.projectIntern.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequestDTO requestDTO);

    @Mapping(target = "roleName", source = "role.name")
    UserResponseDTO toResponseDTO(User user);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true) // Cập nhật password thường làm API riêng
    void updateEntityFromDTO(UserRequestDTO requestDTO, @MappingTarget User user);
}
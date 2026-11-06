package com.example.projectIntern.user.service;

import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.user.dto.UserRequestDTO;
import com.example.projectIntern.user.dto.UserResponseDTO;
import com.example.projectIntern.user.entity.Role;
import com.example.projectIntern.user.entity.User;
import com.example.projectIntern.user.mapper.UserMapper;
import com.example.projectIntern.user.repository.RoleRepository;
import com.example.projectIntern.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // Thư viện mã hóa
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // Inject BCrypt vào đây

    @Override
    public List<UserResponseDTO> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue().stream().map(userMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)); // Dùng Exception xịn
        return userMapper.toResponseDTO(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (requestDTO.getEmail() != null && userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        Role role = roleRepository.findById(requestDTO.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = userMapper.toEntity(requestDTO);
        user.setRole(role);
        user.setIsActive(true);

        // MÃ HÓA PASSWORD bằng BCrypt trước khi lưu xuống DB
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Role role = roleRepository.findById(requestDTO.getRoleId()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        userMapper.updateEntityFromDTO(requestDTO, user);
        user.setRole(role);

        // Lưu ý: Update không cập nhật password ở hàm này (thường có API đổi pass riêng)
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // CHẶN: Không cho phép xóa tài khoản admin mặc định (dựa vào username gốc của bạn)
        if ("admin_tong".equals(user.getUsername())) {
            throw new AppException(ErrorCode.CANNOT_DELETE_DEFAULT_ADMIN);
        }
        user.setIsActive(false);
        userRepository.save(user);
    }
}
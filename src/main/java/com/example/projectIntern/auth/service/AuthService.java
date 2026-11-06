package com.example.projectIntern.auth.service;

import com.example.projectIntern.auth.dto.AuthRequestDTO;
import com.example.projectIntern.auth.dto.AuthResponseDTO;
import com.example.projectIntern.common.exception.AppException;
import com.example.projectIntern.common.exception.ErrorCode;
import com.example.projectIntern.security.JwtService;
import com.example.projectIntern.user.entity.User;
import com.example.projectIntern.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO login(AuthRequestDTO request) {
        // 1. Xác thực Username & Password (Spring Security sẽ tự so sánh băm BCrypt)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new RuntimeException("Sai tài khoản hoặc mật khẩu"); // Lát nữa bắt bằng AppException sau
        }

        // 2. Lấy User từ DB
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 3. Tạo Token
        String jwtToken = jwtService.generateToken(user);

        // 4. Trả về cho Client
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .type("Bearer")
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }
}
package com.example.projectIntern.configuration;

import com.example.projectIntern.security.JwtAccessDeniedHandler;
import com.example.projectIntern.security.JwtAuthenticationEntryPoint;
import com.example.projectIntern.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // --- CẤU HÌNH CORS CHO FRONTEND REACT / VITE ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cổng mặc định của React (3000) và Vite (5173).
        // Dấu * là cho phép tất cả, nhưng ở thực tế nên ghi rõ domain.
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Kích hoạt CORS
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC API (Ai cũng vào được)
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // 2. QUẢN LÝ TÀI KHOẢN (Chỉ Admin)
                        .requestMatchers("/api/v1/users/**").hasAuthority("ROLE_ADMIN")

                        // 3. QUẢN LÝ THUỐC & BÁC SĨ (Ai cũng xem được, nhưng chỉ Admin mới được tạo/xóa)
                        .requestMatchers(HttpMethod.GET, "/api/v1/medicines/**", "/api/v1/doctors/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_RECEPTIONIST")
                        .requestMatchers("/api/v1/medicines/**", "/api/v1/doctors/**").hasAuthority("ROLE_ADMIN")

                        // 4. QUẢN LÝ BỆNH NHÂN (Lễ tân & Admin toàn quyền; Bác sĩ chỉ xem)
                        .requestMatchers(HttpMethod.GET, "/api/v1/patients/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_RECEPTIONIST")
                        .requestMatchers("/api/v1/patients/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPTIONIST")

                        // 5. LỊCH HẸN (Lễ tân toàn quyền; Bác sĩ được phép xem & cập nhật trạng thái)
                        .requestMatchers(HttpMethod.GET, "/api/v1/appointments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_RECEPTIONIST")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/appointments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_RECEPTIONIST")
                        .requestMatchers("/api/v1/appointments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPTIONIST")

                        // 6. CHUYÊN MÔN: BỆNH ÁN & KÊ ĐƠN (Bác sĩ toàn quyền; Lễ tân chỉ xem để tính tiền)
                        .requestMatchers(HttpMethod.GET, "/api/v1/medical-records/**", "/api/v1/prescriptions/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR", "ROLE_RECEPTIONIST")
                        .requestMatchers("/api/v1/medical-records/**", "/api/v1/prescriptions/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR")

                        // 7. HÓA ĐƠN THU TIỀN (Chỉ Lễ tân & Admin được thao tác)
                        .requestMatchers("/api/v1/invoices/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPTIONIST")

                        // Các API khác (nếu có phát sinh) bắt buộc phải đăng nhập
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Bắt lỗi 401
                        .accessDeniedHandler(jwtAccessDeniedHandler)           // Bắt lỗi 403
                );

        return http.build();
    }
}
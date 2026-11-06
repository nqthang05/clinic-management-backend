package com.example.projectIntern.user.entity;

import com.example.projectIntern.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails { // ĐÃ THÊM UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // --- CÁC HÀM CỦA USERDETAILS CẦN OVERRIDE ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về quyền của User (VD: ROLE_ADMIN, ROLE_DOCTOR)
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    // Các hàm kiểm tra trạng thái tài khoản (Ta dùng is_active cho tất cả)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return this.isActive; }
}
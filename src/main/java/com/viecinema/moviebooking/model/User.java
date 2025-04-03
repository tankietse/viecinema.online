package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "email_index", columnList = "email"),
        @Index(name = "username_index", columnList = "username")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Bạn cần nhập Tên người dùng")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Bạn cần nhập mật khẩu")
    @Size(min = 8, message = "Mật khẩu cần ít nhất 8 ký tự hoặc số")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Bạn cần nhập số điện thoại")
    @Size(min = 10, max = 15, message = "Số điện thoại không hợp lệ")
    @Column(name = "phone_number", nullable = false)
    private String phone_number;

    @Email(message = "Email không đúng định dạng")
    @Column(unique = true)
    private String email;

    @Column(name = "locked", nullable = false)
    private boolean locked = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum UserRole {
        ADMIN, CUSTOMER
    }
}

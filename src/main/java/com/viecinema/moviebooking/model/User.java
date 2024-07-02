package com.viecinema.moviebooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

//    @NotBlank(message = "Bạn cần nhập số điện thoại")
    @Size(min = 10, message = "Số điện thoại không hợp lệ")
    @Column(name = "phone_number" , nullable = false)
    private String phone_number;

//    @NotBlank(message = "Dữ liệu bắt buộc")
    @Email(message = "Không đúng định dạng")
    @Column(unique = true)
    private String email;

    @Column(name = "locked", nullable = false)
    private boolean locked = false;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "created_at") // Ánh xạ tới cột "created_at"
    private LocalDateTime createdAt;

    @Column(name = "updated_at") // Ánh xạ tới cột "updated_at"
    private LocalDateTime updatedAt;

    public enum UserRole {
        ADMIN, CUSTOMER, THEATER_OWNER;
    }
}

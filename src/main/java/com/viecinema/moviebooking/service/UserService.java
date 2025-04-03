package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Page<User> findAllByFilters(String username, String email, Pageable pageable) {
        return userRepository.findAllByFilters(username, email, pageable);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Tìm kiếm người dùng theo địa chỉ email
     * @param email Email cần tìm
     * @return Optional<User> người dùng tìm thấy hoặc empty nếu không tìm thấy
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Lưu người dùng mới vào hệ thống, băm mật khẩu và đảm bảo trường bắt buộc
     * @param user Đối tượng người dùng cần lưu
     * @return User đã được lưu
     */
    public User saveUser(User user) {
        // Đảm bảo thông tin bắt buộc được thiết lập
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(LocalDateTime.now());
        }
        
        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Lưu người dùng
        return userRepository.save(user);
    }

    /**
     * Cập nhật thông tin người dùng không thay đổi mật khẩu
     * @param user Đối tượng người dùng cần cập nhật
     * @return User đã được cập nhật
     */
    public User updateUserInfo(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    /**
     * Cập nhật toàn bộ thông tin người dùng bao gồm mật khẩu
     * @param id ID của người dùng
     * @param updateUser Đối tượng người dùng với thông tin mới
     * @return Optional<User> Người dùng sau khi cập nhật
     */
    public Optional<User> updateUser(Long id, User updateUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updateUser.getUsername());
            
            if (updateUser.getEmail() != null && !updateUser.getEmail().isEmpty()) {
                user.setEmail(updateUser.getEmail());
            }
            
            if (updateUser.getPhone_number() != null && !updateUser.getPhone_number().isEmpty()) {
                user.setPhone_number(updateUser.getPhone_number());
            }
            
            user.setUpdatedAt(LocalDateTime.now());
            
            if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
            }
            
            if (updateUser.getRole() != null) {
                user.setRole(updateUser.getRole());
            }
            
            return userRepository.save(user);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void lockUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setLocked(true);
            u.setUpdatedAt(LocalDateTime.now());
            userRepository.save(u);
        }
    }

    public void unlockUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setLocked(false);
            u.setUpdatedAt(LocalDateTime.now());
            userRepository.save(u);
        }
    }

    public User.UserRole getUserRole(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getRole).orElse(null);
    }

    /**
     * Find all users with filtering and pagination
     */
    public Page<User> findAll(String search, String role, String status, Pageable pageable) {
        List<User> allUsers = userRepository.findAll();
        
        List<User> filteredUsers = allUsers.stream()
            .filter(user -> {
                boolean matches = true;
                
                // Filter by search (username, name, email)
                if (search != null && !search.isEmpty()) {
                    String searchLower = search.toLowerCase();
                    matches = matches && (
                        (user.getUsername() != null && user.getUsername().toLowerCase().contains(searchLower)) ||
                        (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchLower)) ||
                        (user.getUsername() != null && user.getUsername().toLowerCase().contains(searchLower))
                    );
                }
                
                // Filter by role
                if (role != null && !role.isEmpty()) {
                    matches = matches && (user.getRole() != null && 
                        user.getRole().toString().equalsIgnoreCase(role));
                }
                
                // Filter by status
                if (status != null && !status.isEmpty()) {
                    boolean isActive = "active".equalsIgnoreCase(status);
                    matches = matches && (user.isLocked() != isActive);
                }
                
                return matches;
            })
            .collect(Collectors.toList());
        
        // Apply sorting from pageable
        if (pageable.getSort().isSorted()) {
            // Implement sorting logic if needed
        }
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredUsers.size());
        
        if (start > filteredUsers.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filteredUsers.size());
        }
        
        return new PageImpl<>(
            filteredUsers.subList(start, end),
            pageable,
            filteredUsers.size()
        );
    }
    
    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get current user ID from security context
     */
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !(authentication instanceof AnonymousAuthenticationToken)) {
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                return user.getId();
            }
        }
        return null;
    }
}

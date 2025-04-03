package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping
    public String showUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/manage_users";
    }
    
    @PostMapping("/create")
    public String createUser(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String phone_number,
                           @RequestParam User.UserRole role,
                           @RequestParam(required = false) Boolean locked,
                           RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra username đã tồn tại chưa
            if (userService.existsByUsername(username)) {
                redirectAttributes.addFlashAttribute("error", "Tên người dùng đã tồn tại");
                return "redirect:/admin/users";
            }
            
            // Tạo user mới
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setPhone_number(phone_number);
            user.setRole(role);
            user.setLocked(locked != null && locked);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "Tạo người dùng mới thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo người dùng: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id,
                         @RequestParam String username,
                         @RequestParam(required = false) String password,
                         @RequestParam String email,
                         @RequestParam String phone_number,
                         @RequestParam User.UserRole role,
                         @RequestParam(required = false) Boolean locked,
                         RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng");
                return "redirect:/admin/users";
            }
            
            User user = userOptional.get();
            
            // Kiểm tra nếu username thay đổi và đã tồn tại
            if (!user.getUsername().equals(username) && userService.existsByUsername(username)) {
                redirectAttributes.addFlashAttribute("error", "Tên người dùng đã tồn tại");
                return "redirect:/admin/users";
            }
            
            user.setUsername(username);
            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password));
            }
            user.setEmail(email);
            user.setPhone_number(phone_number);
            user.setRole(role);
            user.setLocked(locked != null && locked);
            user.setUpdatedAt(LocalDateTime.now());
            
            userService.updateUserInfo(user);
            redirectAttributes.addFlashAttribute("success", "Cập nhật người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật người dùng: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Xóa người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa người dùng: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/lock/{id}")
    public String lockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.lockUser(id);
            redirectAttributes.addFlashAttribute("success", "Khóa người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi khóa người dùng: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/unlock/{id}")
    public String unlockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.unlockUser(id);
            redirectAttributes.addFlashAttribute("success", "Mở khóa người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi mở khóa người dùng: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
}
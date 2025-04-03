package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Lưu trữ mã xác nhận tạm thời (trong thực tế nên lưu trong cơ sở dữ liệu)
    private final Map<String, String> resetTokens = new HashMap<>();
    private final Map<String, LocalDateTime> tokenExpiration = new HashMap<>();

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                       @RequestParam(required = false) String registered,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
        }
        
        if (registered != null && registered.equals("success")) {
            model.addAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập để tiếp tục.");
        }
        
        return "user/login";
    }

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username, 
            @RequestParam String password, 
            @RequestParam String confirmPassword,
            @RequestParam String phone_number,
            @RequestParam(required = false) String email,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu không khớp");
            return "user/register";
        }

        if (userService.existsByUsername(username)) {
            model.addAttribute("error", "Đã tồn tại user này");
            return "user/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone_number(phone_number);
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        user.setRole(User.UserRole.CUSTOMER);
        user.setLocked(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        try {
            userService.saveUser(user);
            redirectAttributes.addAttribute("registered", "success");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "user/register";
        }
    }

    @GetMapping("/forgot-pass")
    public String forgotPassword() {
        return "user/forgot-pass";
    }

    @PostMapping("/forgot-pass")
    public String processForgotPassword(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer step,
            @RequestParam(required = false) String verificationCode,
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {
            
        // Bước 1: Xác thực email
        if (step == null || step == 1) {
            Optional<User> userOptional = userService.getUserByEmail(email);
            
            if (userOptional.isEmpty()) {
                model.addAttribute("error", "Email này chưa được đăng ký");
                return "user/forgot-pass";
            }
            
            // Tạo mã xác nhận ngẫu nhiên
            String verificationToken = generateVerificationCode();
            resetTokens.put(email, verificationToken);
            tokenExpiration.put(email, LocalDateTime.now().plusMinutes(15));
            
            // Trong thực tế, gửi email với mã xác nhận
            // emailService.sendVerificationCode(email, verificationToken);
            
            // Trong môi trường demo, hiển thị mã xác nhận trên màn hình
            model.addAttribute("step", 2);
            model.addAttribute("email", email);
            model.addAttribute("success", "Mã xác nhận đã được gửi đến email của bạn. (Mã demo: " + verificationToken + ")");
            return "user/forgot-pass";
        }
        
        // Bước 2: Xác thực mã
        else if (step == 2) {
            String storedToken = resetTokens.get(email);
            LocalDateTime expirationTime = tokenExpiration.get(email);
            
            if (storedToken == null || expirationTime == null || LocalDateTime.now().isAfter(expirationTime)) {
                model.addAttribute("error", "Mã xác nhận đã hết hạn hoặc không hợp lệ");
                model.addAttribute("step", 1);
                return "user/forgot-pass";
            }
            
            if (!storedToken.equals(verificationCode)) {
                model.addAttribute("error", "Mã xác nhận không chính xác");
                model.addAttribute("step", 2);
                model.addAttribute("email", email);
                return "user/forgot-pass";
            }
            
            model.addAttribute("step", 3);
            model.addAttribute("email", email);
            model.addAttribute("token", storedToken);
            model.addAttribute("success", "Mã xác nhận hợp lệ. Vui lòng tạo mật khẩu mới");
            return "user/forgot-pass";
        }
        
        // Bước 3: Đổi mật khẩu
        else if (step == 3) {
            String storedToken = resetTokens.get(email);
            LocalDateTime expirationTime = tokenExpiration.get(email);
            
            if (storedToken == null || expirationTime == null || LocalDateTime.now().isAfter(expirationTime) || !storedToken.equals(token)) {
                model.addAttribute("error", "Phiên làm việc đã hết hạn. Vui lòng thử lại");
                model.addAttribute("step", 1);
                return "user/forgot-pass";
            }
            
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp");
                model.addAttribute("step", 3);
                model.addAttribute("email", email);
                model.addAttribute("token", token);
                return "user/forgot-pass";
            }
            
            if (newPassword.length() < 8) {
                model.addAttribute("error", "Mật khẩu mới phải có ít nhất 8 ký tự");
                model.addAttribute("step", 3);
                model.addAttribute("email", email);
                model.addAttribute("token", token);
                return "user/forgot-pass";
            }
            
            Optional<User> userOptional = userService.getUserByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setUpdatedAt(LocalDateTime.now());
                userService.updateUserInfo(user);
                
                // Xóa token sau khi sử dụng
                resetTokens.remove(email);
                tokenExpiration.remove(email);
                
                redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được thay đổi thành công. Vui lòng đăng nhập với mật khẩu mới");
                return "redirect:/login";
            } else {
                model.addAttribute("error", "Không tìm thấy người dùng");
                model.addAttribute("step", 1);
                return "user/forgot-pass";
            }
        }
        
        return "user/forgot-pass";
    }

    @GetMapping("/user-profile")
    public String getUserProfile(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.getUserByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
                return "user/user-profile";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/user-profile")
    public String updateUserProfile(User updatedUser, 
                                  Principal principal, 
                                  RedirectAttributes redirectAttributes) {
        if (principal != null) {
            String username = principal.getName();
            User currentUser = userService.getUserByUsername(username).orElse(null);
            
            if (currentUser != null) {
                currentUser.setEmail(updatedUser.getEmail());
                currentUser.setPhone_number(updatedUser.getPhone_number());
                currentUser.setUpdatedAt(LocalDateTime.now());
                userService.updateUserInfo(currentUser);
                redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
            }
        }
        return "redirect:/user-profile";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                               @RequestParam String newPassword,
                               @RequestParam String confirmPassword,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        if (principal != null) {
            String username = principal.getName();
            User currentUser = userService.getUserByUsername(username).orElse(null);
            
            if (currentUser != null) {
                // Kiểm tra mật khẩu hiện tại
                if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
                    redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng");
                    return "redirect:/user-profile";
                }
                
                // Kiểm tra xác nhận mật khẩu
                if (!newPassword.equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp");
                    return "redirect:/user-profile";
                }
                
                // Kiểm tra độ phức tạp của mật khẩu
                if (newPassword.length() < 8) {
                    redirectAttributes.addFlashAttribute("error", "Mật khẩu mới phải có ít nhất 8 ký tự");
                    return "redirect:/user-profile";
                }
                
                // Cập nhật mật khẩu
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                currentUser.setUpdatedAt(LocalDateTime.now());
                userService.updateUserInfo(currentUser);
                
                redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
            }
        }
        return "redirect:/user-profile";
    }
    
    /**
     * Tạo mã xác nhận ngẫu nhiên 6 chữ số
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Tạo số ngẫu nhiên từ 100000 đến 999999
        return String.valueOf(code);
    }
}
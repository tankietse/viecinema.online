package com.viecinema.moviebooking.controller;

import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "user/login";
    }

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
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
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/forgot-pass")
    public String forgotPassword() {
        return "user/forgot-pass";
    }

    @PostMapping("/forgot-pass")
    public String forgotPassword(@RequestParam String email, Model model) {
        // Logic gửi email để reset mật khẩu

        model.addAttribute("message", "Password reset instructions have been sent to your email.");
        return "user/forgot-pass";
    }
}
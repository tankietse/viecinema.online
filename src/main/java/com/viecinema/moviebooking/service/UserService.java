package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Integer id, User updateUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updateUser.getUsername());
            user.setEmail(updateUser.getEmail());
            user.setPhone_number(updateUser.getPhone_number());
            user.setUpdatedAt(updateUser.getUpdatedAt());
            user.setCreatedAt(updateUser.getCreatedAt());
            user.setPassword(
                    passwordEncoder.encode(
                            updateUser.getPassword()
                    )
            );
            user.setRole(updateUser.getRole());
            return userRepository.save(user);
        });
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void lockUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setLocked(true);
            userRepository.save(u);
        }
    }

    public  void unlockUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setLocked(false);
            userRepository.save(u);
        }
    }

    public User.UserRole getUserRole(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(User::getRole).orElse(null);
    }
}

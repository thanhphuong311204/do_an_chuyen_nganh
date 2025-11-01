package com.appQLCT.AppQLCT.service.core;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.appQLCT.AppQLCT.dto.RegisterRequest;
import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService; // chỉ 1 chiều (không còn vòng lặp)

    // ✅ Đăng ký tài khoản mới
    public User registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setActive(true);
        return userRepository.save(user);
    }

    // ✅ Lấy danh sách tất cả user (cho admin)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Khóa / mở khóa user (cho admin)
    public User toggleUserLock(Long userId, boolean lock) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setActive(!lock);
        userRepository.save(user);

        // gửi thông báo cho user
        notificationService.createNotification(
                user,
                lock ? "Tài khoản bị khóa 🔒" : "Tài khoản được mở khóa ✅",
                lock ? "Tài khoản của bạn đã bị khóa bởi quản trị viên."
                        : "Tài khoản của bạn đã được mở khóa, bạn có thể đăng nhập lại.",
                "system"
        );

        return user;
    }

    // ✅ Tìm user theo email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // ✅ Tìm user theo ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId : " + userId));
    }

    // ✅ Load user cho Spring Security
    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(input)
                .orElseGet(() -> userRepository.findByUsername(input)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + input)));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole().toUpperCase())
                .disabled(!user.isActive())
                .build();
    }

    // ✅ Lấy user hiện tại (từ JWT)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Không có người dùng nào đang đăng nhập!");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .or(() -> userRepository.findByUsername(email))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));
    }
}

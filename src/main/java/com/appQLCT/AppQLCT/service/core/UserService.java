package com.appQLCT.AppQLCT.service.core;

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
import com.appQLCT.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 🧾 Đăng ký người dùng mới
     */
    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setActive(true);
        user.setRole("USER");

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 🔍 Tìm user theo email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));
    }

    /**
     * 👤 Lấy user đang đăng nhập
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("Không có Authentication trong context!");
        }

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa xác thực!");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));
    }

    /**
     * 💾 Lưu user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * 🧩 Cập nhật hồ sơ người dùng
     */
    public User updateProfile(String fullName, String phone, String avatarUrl) {
        User user = getCurrentUser();

        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName);
        }

        if (phone != null && !phone.trim().isEmpty()) {
            user.setPhone(phone);
        }

        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            user.setAvatarUrl(avatarUrl);
        }

        return userRepository.save(user);
    }

public void changePassword(String oldPassword, String newPassword) {
    User user = getCurrentUser();

if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
    throw new RuntimeException("Mật khẩu cũ không đúng!");
}
user.setPasswordHash(passwordEncoder.encode(newPassword));
    // ✅ Mã hóa và lưu mật khẩu mới
    user.setPasswordHash(passwordEncoder.encode(newPassword));
    userRepository.save(user);
}    /**
     * 🔑 Xác thực người dùng (dùng cho Spring Security)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + email));

        return new UserDetailsImpl(user);
    }
}

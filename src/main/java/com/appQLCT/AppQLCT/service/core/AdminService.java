package com.appQLCT.AppQLCT.service.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.repository.core.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public User authenticateAdmin(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();

        boolean isPasswordMatch = passwordEncoder.matches(password, user.getPasswordHash());
        boolean isAdmin = user.getRole() != null &&
                          user.getRole().toUpperCase().contains("ADMIN");

        if (isPasswordMatch && isAdmin) {
            return user;
        }

        return null;
    }
}

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
        if (optionalUser.isEmpty()) return null;

        User user = optionalUser.get();

        if (passwordEncoder.matches(password, user.getPasswordHash()) 
                && "ADMIN".equalsIgnoreCase(user.getRole())) {
            System.out.println("Ham oke");
            return user;
        }
        return null;
    }
}

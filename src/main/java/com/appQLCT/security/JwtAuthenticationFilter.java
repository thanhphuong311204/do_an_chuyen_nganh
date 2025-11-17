package com.appQLCT.security;

import com.appQLCT.AppQLCT.service.core.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final @Lazy UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, @Lazy UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String path = request.getServletPath();

        // 1️⃣ Bỏ qua toàn bộ API public
        if (path.startsWith("/api/auth") ||
            path.startsWith("/api/categories")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ Lấy Authorization header
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // ❗ Nếu không có token → bỏ qua để SecurityConfig xử lý
            filterChain.doFilter(request, response);
            return;
        }

        // 3️⃣ Lấy JWT từ header
        final String jwt = authHeader.substring(7).trim();

        if (jwt.isEmpty()) {
            // ❗ Token trống → bỏ qua (không trả 403)
            filterChain.doFilter(request, response);
            return;
        }

        final String userEmail;
        try {
            userEmail = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            System.out.println("❌ Lỗi decode JWT: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // 4️⃣ Không để trùng authentication
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } else {
                System.out.println("❌ Token không hợp lệ hoặc hết hạn!");
            }
        }

        // 5️⃣ Tiếp tục filter chain
        filterChain.doFilter(request, response);
    }
}

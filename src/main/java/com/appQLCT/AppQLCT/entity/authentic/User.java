package com.appQLCT.AppQLCT.entity.authentic;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.appQLCT.AppQLCT.enu.AuthProvider;

@Entity
@Table(name = "users") 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false, length = 100)
    private String username;

    @Column( name = "email",nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "auth_provider")
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.email;

    @Column(name = "provider_uid")
    private String providerUid;
    @Builder.Default
    @Column(name = "phone", length = 20)
    private String phone = null;

    @Builder.Default
    @Column(nullable = false)
    private String role = "USER";

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;


    @Column(name = "biometric_enable")
    @Builder.Default
    private int biometricEnable = 0;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "full_name")
    private String fullName;


    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

}

package com.appQLCT.AppQLCT.controller.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.service.core.CloudinaryService;
import com.appQLCT.AppQLCT.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;
    private final UserService userService;

    @PostMapping(
        value = "/image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Không có file nào được gửi!"
                ));
            }

            String imageUrl = cloudinaryService.uploadFile(file);

            User currentUser = userService.getCurrentUser();

            currentUser.setAvatarUrl(imageUrl);
            userService.save(currentUser); 

            return ResponseEntity.ok(Map.of(
                    "message", "Upload avatar thành công!",
                    "avatarUrl", imageUrl
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Upload thất bại",
                    "message", e.getMessage()
            ));
        }
    }
}

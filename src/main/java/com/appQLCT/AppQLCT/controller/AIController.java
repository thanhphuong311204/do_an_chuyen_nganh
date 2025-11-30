package com.appQLCT.AppQLCT.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appQLCT.AppQLCT.service.ai.AIService;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/suggest-category")
    public ResponseEntity<?> suggestCategory(@RequestBody Map<String, String> req) {
        String text = req.get("description");

        // Gọi AIService đúng chuẩn
        Map<String, Object> result = aiService.suggestCategory(text);

        return ResponseEntity.ok(result);
    }
}

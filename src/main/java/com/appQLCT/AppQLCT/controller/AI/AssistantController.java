package com.appQLCT.AppQLCT.controller.ai;

import com.appQLCT.AppQLCT.dto.AssistantRequest;
import com.appQLCT.AppQLCT.service.ai.AssistantService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

@PostMapping("/ask")
public ResponseEntity<?> askAssistant(@RequestBody AssistantRequest req) {

    String reply = assistantService.generateReply(
            req.getQuestion(),
            req.getMode()
    );

    return ResponseEntity.ok(Map.of("reply", reply));
}
}
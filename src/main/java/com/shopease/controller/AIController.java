package com.shopease.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopease.service.GeminiService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    private final GeminiService geminiService;

    public AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(
            @RequestBody Map<String, String> request) {

        String question = request.get("question");

        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please enter your question."));
        }

        String answer = geminiService.getShoppingAdvice(question);

        return ResponseEntity.ok(Map.of("answer", answer));
    }
}
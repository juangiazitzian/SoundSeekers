package com.uade.soundseekers.controllers.auth;

import com.uade.soundseekers.dto.MessageResponseDto;
import com.uade.soundseekers.dto.PasswordResetRequestDto;
import com.uade.soundseekers.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://front-seminario.s3-website.us-east-2.amazonaws.com/")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDto> forgotPassword(@RequestParam("email") String email) {
        MessageResponseDto response = passwordResetService.sendPasswordResetToken(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDto> resetPassword(@RequestBody PasswordResetRequestDto request) {
        MessageResponseDto response = passwordResetService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
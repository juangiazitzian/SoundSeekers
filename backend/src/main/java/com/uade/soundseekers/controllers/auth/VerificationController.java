package com.uade.soundseekers.controllers.auth;

import com.uade.soundseekers.dto.MessageResponseDto;
import com.uade.soundseekers.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://front-seminario.s3-website.us-east-2.amazonaws.com/")
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/verify")
    public MessageResponseDto verifyEmail(@RequestParam("token") String token, @RequestParam("email") String email) {
        return verificationService.verifyEmail(token, email);
    }

    @PostMapping("/resend-verification")
    public MessageResponseDto resendVerification(@RequestParam("email") String email) {
        return verificationService.resendVerification(email);
    }
}
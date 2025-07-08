package com.uade.soundseekers.service;

import com.uade.soundseekers.dto.MessageResponseDto;
import com.uade.soundseekers.entity.User;
import com.uade.soundseekers.entity.VerificationToken;
import com.uade.soundseekers.exception.BadRequestException;
import com.uade.soundseekers.exception.NotFoundException;
import com.uade.soundseekers.repository.UserRepository;
import com.uade.soundseekers.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailSender;

    public MessageResponseDto verifyEmail(String token, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No existe un usuario con ese email");
        }

        User user = optionalUser.get();

        if (user.isEmailVerified()) {
            throw new BadRequestException("La cuenta ya fue verificada");
        }

        VerificationToken verificationToken = tokenRepository.findByUser(user);
        if (!verificationToken.getToken().equals(token) || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token inválido");
        }

        user.setEmailVerified(true);
        userRepository.save(user);
        return new MessageResponseDto("Email verficiado exitosamente");
    }

    public MessageResponseDto resendVerification(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No existe un usuario con ese email");
        }

        User user = optionalUser.get();

        if (user.isEmailVerified()) {
            throw new BadRequestException("La cuenta ya fue verificada");
        }

        VerificationToken existingToken = tokenRepository.findByUser(user);
        tokenRepository.delete(existingToken);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);
        try {
            emailSender.sendVerificationEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de verificación", e);
        }
        return new MessageResponseDto("Correo de verificación enviado exitosamente.");
    }
}
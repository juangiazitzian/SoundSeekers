package com.uade.soundseekers.service;

import com.uade.soundseekers.dto.MessageResponseDto;
import com.uade.soundseekers.dto.PasswordResetRequestDto;
import com.uade.soundseekers.entity.User;
import com.uade.soundseekers.entity.VerificationToken;
import com.uade.soundseekers.exception.BadRequestException;
import com.uade.soundseekers.exception.NotFoundException;
import com.uade.soundseekers.repository.UserRepository;
import com.uade.soundseekers.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailSender;
    private final PasswordEncoder passwordEncoder;

    // Enviar el token para restablecer la contraseña
    public MessageResponseDto sendPasswordResetToken(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No existe un usuario con ese email.");
        }
        User user = optionalUser.get();

        // Verificar y eliminar cualquier token existente
        VerificationToken existingToken = tokenRepository.findByUser(user);
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);
        
        try {
            emailSender.sendPasswordResetEmail(user.getEmail(), token);
        } catch (Exception e) {
            throw new BadRequestException("Error al enviar el correo de verificación: " + e.getMessage());
        }
        
        
        return new MessageResponseDto("El token para restablecer la contraseña se ha enviado correctamente.");
    }

    // Restablecer la contraseña
    public MessageResponseDto resetPassword(PasswordResetRequestDto request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No existe un usuario con ese email.");
        }

        User user = optionalUser.get();

        // Validar el token
        VerificationToken verificationToken = tokenRepository.findByUser(user);
        if (verificationToken == null || !verificationToken.getToken().equals(request.getToken()) || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token inválido o expirado.");
        }

        // Validar la nueva contraseña
        if (!isValidPassword(request.getPassword())) {
            throw new BadRequestException("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.");
        }

        // Actualizar la contraseña del usuario
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new BadRequestException("Error al guardar la nueva contraseña: " + e.getMessage());
        }

        return new MessageResponseDto("Contraseña restablecida correctamente.");
    }

    // Método para validar la contraseña
    private boolean isValidPassword(String password) {
        String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        return pattern.matcher(password).matches();
    }
}

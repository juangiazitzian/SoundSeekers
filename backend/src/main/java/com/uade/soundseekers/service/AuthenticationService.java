package com.uade.soundseekers.service;

import com.uade.soundseekers.dto.AuthenticationRequest;
import com.uade.soundseekers.dto.AuthenticationResponse;
import com.uade.soundseekers.dto.RegisterRequestDTO;
import com.uade.soundseekers.config.JwtService;
import com.uade.soundseekers.dto.MessageResponseDto;
import com.uade.soundseekers.entity.Localidad;
import com.uade.soundseekers.entity.MusicGenre;
import com.uade.soundseekers.entity.User;
import com.uade.soundseekers.entity.VerificationToken;
import com.uade.soundseekers.exception.BadRequestException;
import com.uade.soundseekers.exception.NotFoundException;
import com.uade.soundseekers.repository.LocalidadRepository;
import com.uade.soundseekers.repository.UserRepository;
import com.uade.soundseekers.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailSender;
    private final LocalidadRepository localidadRepository;

    private static final String EMAIL_REGEX = "^(?!.*\\.\\..)(?!.*\\.$)(?!^\\.)[A-Za-z0-9][A-Za-z0-9._%+-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$"; //restricciones del mail
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$"; // Minimum 8 characters, one uppercase, one lowercase, one number

    public MessageResponseDto register(RegisterRequestDTO request) {
        if (!isValidEmail(request.getEmail())) {
            throw new BadRequestException("El email proporcionado no es válido.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("El email ya está registrado.");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("El nombre de usuario ya está registrado.");
        }

        if (!isValidPassword(request.getPassword())) {
            throw new BadRequestException("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.");
        }

        User user = User.builder()
            .email(request.getEmail())
            .name(request.getName())
            .password(passwordEncoder.encode(request.getPassword()))
            .lastName(request.getLastName())
            .username(request.getUsername())
            .edad(request.getEdad())
            .isEmailVerified(false)
            .role(request.getRole())
            .build();

        Localidad localidad = localidadRepository.findById(request.getLocalidadId())
            .orElseThrow(() -> new RuntimeException("Localidad con ID: " + request.getLocalidadId()+" no existe"));
        user.setLocalidad(localidad);

        List<MusicGenre> generosMusicales = request.getGenerosMusicalesPreferidos().stream()
            .map(genre -> {
                try {
                    return MusicGenre.valueOf(genre.name().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Género inválido: " + genre);
                }
            })
            .collect(Collectors.toList());
        user.setGenerosMusicalesPreferidos(generosMusicales);

        userRepository.save(user);

        // Generar y guardar el token de verificación
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);

        // Enviar el email de verificación
        try {
            emailSender.sendVerificationEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de verificación", e);
        }

        return new MessageResponseDto("Usuario registrado con éxito. Se ha enviado un email de verificación a " + user.getEmail());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("No existe un usuario registrado con el email proporcionado.");
        }

        User user = optionalUser.get();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new BadRequestException("La contraseña es incorrecta.");
        }

        if (!user.isEmailVerified()) {
            throw new BadRequestException("El email no ha sido verificado.");
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .role(user.getRole().name())
            .userId(user.getId())
            .build();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        return pattern.matcher(password).matches();
    }
}
package com.uade.soundseekers.dto;

import lombok.Data;

@Data
public class PasswordResetRequestDto {
    private String token;
    private String email;
    private String password;
}
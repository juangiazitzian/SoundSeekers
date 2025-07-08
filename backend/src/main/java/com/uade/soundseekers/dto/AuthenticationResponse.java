package com.uade.soundseekers.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String accessToken;
    private String role;
    private Long userId;
}

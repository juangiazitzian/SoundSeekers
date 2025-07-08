package com.uade.soundseekers.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserDTO {
    private String name;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private Integer edad;
    private String role;
    private Long localidadId;
    private List<String> genres;
}
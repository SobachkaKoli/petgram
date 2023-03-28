package com.example.petgram.DTO;

import com.example.petgram.model.Role;
import lombok.Data;

@Data
public class OAuth2UserDto {
    private String token;
    private Role role;
}

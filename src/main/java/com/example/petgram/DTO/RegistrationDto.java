package com.example.petgram.DTO;

import com.example.petgram.model.Role;
import lombok.Data;

@Data
public class RegistrationDto {

    private String username;
    private String password;
    private String email;
    private Role role;

}

package com.example.petgram.DTO;

import com.example.petgram.model.AuthProvider;
import com.example.petgram.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserDto {

    private String id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private Role role;
    private AuthProvider provider;
    private String providerId;

}
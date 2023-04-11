package com.example.petgram.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserDto {

    private String email;
    private String username;
    private String password;


}
package com.example.petgram.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    private String username;

    private String email;

    private String password;

}

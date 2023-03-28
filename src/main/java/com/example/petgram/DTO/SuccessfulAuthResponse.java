package com.example.petgram.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SuccessfulAuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserDto user;
}

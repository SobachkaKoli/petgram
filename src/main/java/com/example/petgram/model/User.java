package com.example.petgram.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    private String avatar;
    private Role role;
    private int following;
    private int followers;
    private AuthProvider provider;
    private String providerId;
}

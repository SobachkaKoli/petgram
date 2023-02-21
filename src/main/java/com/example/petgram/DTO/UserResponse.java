package com.example.petgram.DTO;

import com.example.petgram.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {

    private String name;
    private String avatar;
    private List<User> friends;

}
//Доробити Юзер респонс
// список друзів( підписки і підписники) і їх кількість
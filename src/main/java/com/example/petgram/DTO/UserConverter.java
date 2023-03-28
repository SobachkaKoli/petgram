package com.example.petgram.DTO;

import com.example.petgram.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final ModelMapper modelMapper;

    public UserConverter() {
        this.modelMapper = new ModelMapper();
    }

    public UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}

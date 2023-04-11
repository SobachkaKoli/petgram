package com.example.petgram.service.serviceImpl;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.Role;
import com.example.petgram.model.User;
import com.example.petgram.service.AdminService;
import com.example.petgram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserService userService;


    public AdminServiceImpl(UserService userService) {
        this.userService = userService;

    }




    @Override
    public User banUserById(String username) throws Status430UserNotFoundException, Status444UserIsNull {
        if (!userService.existsByUsername(username)){
            log.info("User {} not found", username);
            throw new Status430UserNotFoundException(username);
        }else {
            User user = userService.findByUsername(username);
            user.setRole(Role.BANNED_USER);
            log.info("User {} was baned", username);
           return userService.save(user);
        }
    }

    @Override
    public User unBanUserById(String username) throws Status430UserNotFoundException, Status444UserIsNull {
        if (!userService.existsByUsername(username)){
            log.info("User {} not found", username);
            throw new Status430UserNotFoundException(username);
        }else {
            User user = userService.findByUsername(username);
            user.setRole(Role.USER);
            log.info("User {} was unbanned", username);
            return userService.save(user);
        }
    }
}

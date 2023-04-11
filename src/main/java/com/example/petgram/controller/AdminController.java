package com.example.petgram.controller;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;
import com.example.petgram.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }
    @PatchMapping("/ban-user/{userId}")
    public User banUser(@PathVariable String userId ) throws Status430UserNotFoundException, Status444UserIsNull {
        return adminService.banUserById(userId);
    }
    @PatchMapping("/unban-user/{userId}")
    public User unBanUser(@PathVariable String userId ) throws Status430UserNotFoundException, Status444UserIsNull {
        return adminService.unBanUserById(userId);
    }

}

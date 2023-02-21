package com.example.petgram.service;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;

public interface AdminService {
    User banUserById(String userId) throws Status430UserNotFoundException, Status444UserIsNull;
    User unBanUserById(String userId) throws Status430UserNotFoundException, Status444UserIsNull;

}

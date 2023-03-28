package com.example.petgram.service;

import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status443FileIsNullException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.security.jwt.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarService {
    String setAvatar(MultipartFile file, UserPrincipal userPrincipal) throws IOException, Status443FileIsNullException, Status444UserIsNull, Status430UserNotFoundException;
}

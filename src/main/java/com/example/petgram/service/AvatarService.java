package com.example.petgram.service;

import com.example.petgram.Exception.Status443FileIsNullException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.security.JwtUser;
import org.apache.catalina.webresources.JarWarResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarService {
    String setAvatar(MultipartFile file, JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status444UserIsNull;
}

package com.example.petgram.service;

import com.example.petgram.Exception.Status443FileIsNullException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.security.JwtUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {

    String savePicture(MultipartFile multipartFile,String dir) throws IOException, Status443FileIsNullException;

    String createPostPicturePath(JwtUser jwtUser) throws Status444UserIsNull;
    String createAvatarPath(JwtUser jwtUser) throws Status444UserIsNull;
}

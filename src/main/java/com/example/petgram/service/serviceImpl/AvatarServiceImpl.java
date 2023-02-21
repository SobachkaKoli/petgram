package com.example.petgram.service.serviceImpl;


import com.example.petgram.Exception.Status443FileIsNullException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.User;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.AvatarService;
import com.example.petgram.service.PictureService;
import com.example.petgram.service.UserService;
import jdk.jfr.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class AvatarServiceImpl implements AvatarService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserService userService;
    private final PictureService pictureService;
    private final UserRepository userRepository;

    public AvatarServiceImpl(UserService userService, PictureService pictureService, UserRepository userRepository) {
        this.userService = userService;
        this.pictureService = pictureService;
        this.userRepository = userRepository;
    }

    @Override
    public String setAvatar(MultipartFile file, JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status444UserIsNull {

        String picturePath = pictureService.savePicture(file,pictureService.createAvatarPath(jwtUser));
        log.info("Picture path {} :", picturePath);
        User user = userRepository.findByUserName(userService.getAuthenticatedUser(jwtUser).getUserName()).orElseThrow();
        log.info("User {} is found", user);
        user.setAvatar(picturePath);
        userService.save(user);
        log.info("User {} is saved", user);
        return picturePath;
    }
}

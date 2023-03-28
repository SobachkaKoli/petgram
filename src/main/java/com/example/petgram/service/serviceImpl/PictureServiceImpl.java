package com.example.petgram.service.serviceImpl;


import com.example.petgram.Exception.Status430UserNotFoundException;
import com.example.petgram.Exception.Status443FileIsNullException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.security.jwt.UserPrincipal;
import com.example.petgram.service.PictureService;
import com.example.petgram.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    public PictureServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String savePicture(MultipartFile file,String dir) throws IOException, Status443FileIsNullException {

        if (file != null && !file.getOriginalFilename().isEmpty()){
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            String imgPath = dir + resultFileName;
            file.transferTo(new File(imgPath));
            return imgPath;
        }else {
            throw new Status443FileIsNullException("File is null");
        }
    }

    @Override
    public String createPostPicturePath(UserPrincipal userPrincipal) throws Status444UserIsNull {
        return uploadPath + "/" + userPrincipal.getUsername()
                + "/posts/";
    }

    @Override
    public String createAvatarPath(UserPrincipal userPrincipal) throws Status444UserIsNull, Status430UserNotFoundException {
         return uploadPath + "/" + userService.getAuthenticatedUser(userPrincipal).getUsername()
                + "/avatars/";
    }


}

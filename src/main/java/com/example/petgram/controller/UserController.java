package com.example.petgram.controller;


import com.example.petgram.DTO.UserDTO;
import com.example.petgram.Exception.*;
import com.example.petgram.model.User;
import com.example.petgram.notifications.Notification;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.AvatarService;
import com.example.petgram.service.FriendShipService;
import com.example.petgram.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final FriendShipService friendShipService;
    private final AvatarService avatarService;

    @Autowired
    public UserController(UserService userService, FriendShipService friendShipService, AvatarService avatarService) {
        this.userService = userService;
        this.friendShipService = friendShipService;
        this.avatarService = avatarService;
    }

    @GetMapping("/users")
    public List<User> getAll(){return userService.getUsers();}

    @PostMapping("/registration")
    public User saveUser(@RequestBody UserDTO userDTO) throws Status434UserNicknameNotUniqueException {
        return userService.registerUser(userDTO);

    }

    @PostMapping("/set-avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file,
                                            JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status444UserIsNull {
        return avatarService.setAvatar(file, jwtUser);
    }

    @GetMapping("/get-user-by-username/{username}")
    public User getUserByUsername(@PathVariable String username) throws Status444UserIsNull {
        return userService.getByUserName(username);
    }


    @PostMapping("/follow-up/{followingUsername}")
    public void followUp(@PathVariable String followingUsername, JwtUser jwtUser) throws Status430UserNotFoundException, Status433FriendShipAlreadyExistsException, Status432SelfFollowingException, Status444UserIsNull {
        friendShipService.followUp(followingUsername, jwtUser);
    }

    @GetMapping("/get-notifications")
    public List<Notification> getMyNotifications(JwtUser jwtUser) throws Status444UserIsNull {
        return  userService.getMyNotifications(jwtUser);
    }

    @DeleteMapping("/user/unfollow-up/{followingUsername}")
    public void unFollow(@PathVariable String followingUsername, JwtUser jwtUser) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull {
        friendShipService.unFollow(followingUsername, jwtUser);
    }

    @PatchMapping("/delete-account")
    public User deleteAccount(JwtUser jwtUser) throws Exception {
        return userService.deleteMyAccount(jwtUser);
    }

    @DeleteMapping("/delete-follower/{followerUsername}")
    public void deleteFollower(@PathVariable String followerUsername, JwtUser jwtUser) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull {
        friendShipService.deleteFollower(followerUsername, jwtUser);
    }

    @GetMapping("/get-followers-user/{username}")
    public List<User> getFollowersByUserId(@PathVariable String username) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull {
        return friendShipService.getFollowersByUsername(username);
    }

    @GetMapping("/get-following-user/{username}")
    public List<User> getFollowingByUsername(@PathVariable String username) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull {
        return friendShipService.getFollowingByUsername(username);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
       userService.refreshToken(request,response);
    }






}


package com.example.petgram.service.serviceImpl;

import com.example.petgram.Exception.*;
import com.example.petgram.model.ContentType;
import com.example.petgram.model.FriendShip;
import com.example.petgram.model.User;
import com.example.petgram.repository.FriendShipRepository;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.FriendShipService;
import com.example.petgram.service.NotificationService;
import com.example.petgram.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FriendShipServiceImpl implements FriendShipService{
    private final FriendShipRepository friendShipRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    public FriendShipServiceImpl(FriendShipRepository friendShipRepository, UserService userService, UserRepository userRepository, NotificationService notificationService) {
        this.friendShipRepository = friendShipRepository;
        this.userService = userService;

        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }
    @Override
    public void countFollowersAndFollowing(String followingUserName, JwtUser jwtUser) throws Status430UserNotFoundException, Status444UserIsNull {
        if (!userRepository.existsByUserName(followingUserName)){
            throw new Status430UserNotFoundException(followingUserName);
        }else{
                User follower = userService.getAuthenticatedUser(jwtUser);
                follower.setFollowing(friendShipRepository.countAllByFollower(follower));
                userRepository.save(follower);

               User following = userService.getByUserName(followingUserName);
                following.setFollowers(friendShipRepository.countAllByFollowing(following));
                userRepository.save(following);
        }
    }

    @Override
    public void followUp(String followingUsername, JwtUser jwtUser) throws Status430UserNotFoundException, Status432SelfFollowingException, Status433FriendShipAlreadyExistsException, Status444UserIsNull {

        if (friendShipRepository.existsByFollowerAndFollowing(
                userService.getAuthenticatedUser(jwtUser),userService.getByUserName(followingUsername))){
            throw new Status433FriendShipAlreadyExistsException(followingUsername);
        }else {
            if(!userService.existsByUsername(followingUsername)){
                throw new Status430UserNotFoundException(followingUsername);
            }else if(userService.getAuthenticatedUser(jwtUser).getId().equals(followingUsername)){
                throw new Status432SelfFollowingException("You can not follow up yourself");
            }else {
                FriendShip friendShip = FriendShip
                        .builder()
                        .following(userService.getByUserName(followingUsername))
                        .follower(userService.getAuthenticatedUser(jwtUser))
                        .build();
                friendShipRepository.save(friendShip);
                countFollowersAndFollowing(followingUsername,jwtUser);
                notificationService.sendNotification(
                        jwtUser,followingUsername,userService.getByUserName(followingUsername).getUserName() + " now following you",userService.getAuthenticatedUser(jwtUser).getId(), ContentType.FOLLOWER);
            }
        }
    }
    @Override
    public void unFollow(String followingUsername, JwtUser jwtUser) throws Status442FriendShipDoesntExistsException, Status430UserNotFoundException, Status444UserIsNull {
        if (!userRepository.existsByUserName(followingUsername)) {
            throw new Status430UserNotFoundException(followingUsername);
        } else {
            if (!friendShipRepository.existsByFollowerAndFollowing(userService.getAuthenticatedUser(jwtUser),
                    userService.getByUserName(followingUsername)
            )) {
                throw new Status442FriendShipDoesntExistsException(userService.getByUserName(followingUsername).getUserName());
            } else {
                friendShipRepository.deleteFriendShipByFollowerAndFollowing(userService.getAuthenticatedUser(jwtUser),
                        userService.getByUserName(followingUsername));
                countFollowersAndFollowing(followingUsername, jwtUser);
            }
        }
    }
    @Override
    public List<User> getFollowersByUsername(String username) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull {

        if(!userService.existsByUsername(username)){
            throw new Status430UserNotFoundException(username);
        }else {
            User user = userService.getByUserName(username);
            if (!friendShipRepository.existsByFollowing(user)) {
                throw new Status442FriendShipDoesntExistsException(user.getUserName());
            }else {

                List<User> followers = new ArrayList<>();
                for (FriendShip friendShip : friendShipRepository.findAllByFollowing(user)) {
                    followers.add(friendShip.getFollower());
                }
                return followers;
            }
        }
    }




    @Override
    public List<User> getFollowingByUsername(String username) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull {
        if (!userService.existsByUsername(username)){
            throw new Status430UserNotFoundException(username);
        }else {
            User user = userService.getByUserName(username);
            if(!friendShipRepository.existsByFollower(user)){
                throw new Status442FriendShipDoesntExistsException(user.getUserName());
            }
            List<User> following = new ArrayList<>();
            friendShipRepository.findAllByFollower(user).forEach(friendShip -> following.add(friendShip.getFollowing()));
            return following;
        }
    }
    @Override
    public void deleteFollower(String followingUsername, JwtUser jwtUser) throws Status430UserNotFoundException, Status444UserIsNull {
        if (friendShipRepository.existsByFollowerAndFollowing(
                userService.getByUserName(followingUsername),userService.getAuthenticatedUser(jwtUser))){
            friendShipRepository.deleteFriendShipByFollowerAndFollowing(
                    userService.getByUserName(followingUsername),userService.getAuthenticatedUser(jwtUser));
           countFollowersAndFollowing(followingUsername,jwtUser);
        }else {
            throw new Status430UserNotFoundException(followingUsername);
        }
    }
}

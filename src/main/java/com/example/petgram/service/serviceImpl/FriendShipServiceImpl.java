package com.example.petgram.service.serviceImpl;

import com.example.petgram.Exception.*;
import com.example.petgram.model.ContentType;
import com.example.petgram.model.FriendShip;
import com.example.petgram.model.User;
import com.example.petgram.repository.FriendShipRepository;
import com.example.petgram.repository.UserRepository;
import com.example.petgram.security.jwt.UserPrincipal;
import com.example.petgram.service.FriendShipService;
import com.example.petgram.service.NotificationService;
import com.example.petgram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
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
    public void countFollowersAndFollowing(String followingUserName, UserPrincipal userPrincipal) throws Status430UserNotFoundException, Status444UserIsNull {
        if (!userRepository.existsByUsername(followingUserName)){
            throw new Status430UserNotFoundException(followingUserName);
        }else{
                User follower = userService.getAuthenticatedUser(userPrincipal);
                log.info("Follower {}", follower);
                follower.setFollowing(friendShipRepository.countAllByFollower(follower));
                userRepository.save(follower);

                User following = userService.findByUsername(followingUserName);
                log.info("Following {}", following);
                following.setFollowers(friendShipRepository.countAllByFollowing(following));
                userRepository.save(following);
        }
    }

    @Override
    public void followUp(String followingUsername, UserPrincipal userPrincipal) throws Status430UserNotFoundException, Status432SelfFollowingException, Status433FriendShipAlreadyExistsException, Status444UserIsNull {

        if (friendShipRepository.existsByFollowerAndFollowing(
                userService.getAuthenticatedUser(userPrincipal),userService.findByUsername(followingUsername))){
            throw new Status433FriendShipAlreadyExistsException(followingUsername);
        }else {
            if(!userService.existsByUsername(followingUsername)){
                throw new Status430UserNotFoundException(followingUsername);
            }else if(userService.getAuthenticatedUser(userPrincipal).getId().equals(followingUsername)){
                throw new Status432SelfFollowingException("You can not follow up yourself");
            }else {
                FriendShip friendShip = FriendShip
                        .builder()
                        .following(userService.findByUsername(followingUsername))
                        .follower(userService.getAuthenticatedUser(userPrincipal))
                        .build();
                friendShipRepository.save(friendShip);
                countFollowersAndFollowing(followingUsername, userPrincipal);
                notificationService.sendNotification(
                        userPrincipal,followingUsername,userService.findByUsername(followingUsername).getUsername() + " now following you",userService.getAuthenticatedUser(userPrincipal).getId(), ContentType.FOLLOWER);
            }
        }
    }
    @Override
    public void unFollow(String followingUsername, UserPrincipal userPrincipal) throws Status442FriendShipDoesntExistsException, Status430UserNotFoundException, Status444UserIsNull {
        if (!userRepository.existsByUsername(followingUsername)) {
            throw new Status430UserNotFoundException(followingUsername);
        } else {
            if (!friendShipRepository.existsByFollowerAndFollowing(userService.getAuthenticatedUser(userPrincipal),
                    userService.findByUsername(followingUsername)
            )) {
                throw new Status442FriendShipDoesntExistsException(userService.findByUsername(followingUsername).getUsername());
            } else {
                friendShipRepository.deleteFriendShipByFollowerAndFollowing(userService.getAuthenticatedUser(userPrincipal),
                        userService.findByUsername(followingUsername));
                countFollowersAndFollowing(followingUsername, userPrincipal);
            }
        }
    }
    @Override
    public List<User> getFollowersByUsername(String username) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull {

        if(!userService.existsByUsername(username)){
            throw new Status430UserNotFoundException(username);
        }else {
            User user = userService.findByUsername(username);
            if (!friendShipRepository.existsByFollowing(user)) {
                throw new Status442FriendShipDoesntExistsException(user.getUsername());
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
            User user = userService.findByUsername(username);
            if(!friendShipRepository.existsByFollower(user)){
                throw new Status442FriendShipDoesntExistsException(user.getUsername());
            }
            List<User> following = new ArrayList<>();
            friendShipRepository.findAllByFollower(user).forEach(friendShip -> following.add(friendShip.getFollowing()));
            return following;
        }
    }
    @Override
    public void deleteFollower(String followingUsername, UserPrincipal userPrincipal) throws Status430UserNotFoundException, Status444UserIsNull {
        if (friendShipRepository.existsByFollowerAndFollowing(
                userService.findByUsername(followingUsername),userService.getAuthenticatedUser(userPrincipal))){
            friendShipRepository.deleteFriendShipByFollowerAndFollowing(
                    userService.findByUsername(followingUsername),userService.getAuthenticatedUser(userPrincipal));
           countFollowersAndFollowing(followingUsername, userPrincipal);
        }else {
            throw new Status430UserNotFoundException(followingUsername);
        }
    }
}

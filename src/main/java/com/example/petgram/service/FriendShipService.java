package com.example.petgram.service;

import com.example.petgram.Exception.*;
import com.example.petgram.model.User;
import com.example.petgram.security.JwtUser;

import java.util.List;

public interface FriendShipService {
   void followUp(String followingUsername, JwtUser jwtUser) throws Status430UserNotFoundException, Status432SelfFollowingException, Status433FriendShipAlreadyExistsException, Status444UserIsNull;

   void unFollow(String followingUsername , JwtUser jwtUser) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull;

   List<User> getFollowersByUsername(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull;
   List<User> getFollowingByUsername(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull;

   void countFollowersAndFollowing(String followingId, JwtUser jwtUser) throws Status430UserNotFoundException, Status444UserIsNull;
   void deleteFollower(String followerUsername, JwtUser jwtUser) throws Status430UserNotFoundException, Status444UserIsNull;

}

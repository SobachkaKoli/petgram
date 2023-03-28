package com.example.petgram.service;

import com.example.petgram.Exception.*;
import com.example.petgram.model.User;
import com.example.petgram.security.jwt.UserPrincipal;

import java.util.List;

public interface FriendShipService {
   void followUp(String followingUsername, UserPrincipal userPrincipal) throws Status430UserNotFoundException, Status432SelfFollowingException, Status433FriendShipAlreadyExistsException, Status444UserIsNull;

   void unFollow(String followingUsername , UserPrincipal userPrincipal) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull;

   List<User> getFollowersByUsername(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull;
   List<User> getFollowingByUsername(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException, Status444UserIsNull;

   void countFollowersAndFollowing(String followingId, UserPrincipal userPrincipal) throws Status430UserNotFoundException, Status444UserIsNull;
   void deleteFollower(String followerUsername, UserPrincipal userPrincipal) throws Status430UserNotFoundException, Status444UserIsNull;

}

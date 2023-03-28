package com.example.petgram.service;

import com.example.petgram.DTO.PostDTO;
import com.example.petgram.Exception.*;
import com.example.petgram.model.Comment;
import com.example.petgram.model.Post;
import com.example.petgram.model.SponsorPost;
import com.example.petgram.security.jwt.UserPrincipal;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post createPost(PostDTO postDTO, UserPrincipal userPrincipal) throws IOException, Status443FileIsNullException, Status444UserIsNull, Status430UserNotFoundException;

    SponsorPost createSponsorPost(PostDTO postDTO, String sponsorName, UserPrincipal userPrincipal) throws IOException, Status443FileIsNullException, Status430UserNotFoundException, Status444UserIsNull;
    Post getById(String postId) throws Status440PostNotFoundException;
    void deleteById(String postId, UserPrincipal userPrincipal) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, Status444UserIsNull, Status430UserNotFoundException;
    Post save(Post post);
    Post updatePost(PostDTO postDTO, String postId, UserPrincipal userPrincipal) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, IOException, Status443FileIsNullException, Status444UserIsNull, Status445PictureIsNull, Status430UserNotFoundException;
    boolean userIsPostAuthor(String postId, UserPrincipal userPrincipal) throws Exception;

    List<Post> getAllByAuthenticated(UserPrincipal userPrincipal) throws Exception;

    List<Comment> getCommentsByPostId(String postId) throws Status440PostNotFoundException;
    boolean existsById(String postId);


    List<Post> getAllByUsername(String username) throws Status444UserIsNull;


}

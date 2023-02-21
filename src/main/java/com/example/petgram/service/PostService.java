package com.example.petgram.service;

import com.example.petgram.DTO.PostDTO;
import com.example.petgram.Exception.*;
import com.example.petgram.model.Comment;
import com.example.petgram.model.Post;
import com.example.petgram.model.SponsorPost;
import com.example.petgram.security.JwtUser;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post createPost(PostDTO postDTO, JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status444UserIsNull;

    SponsorPost createSponsorPost(PostDTO postDTO, String sponsorName, JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status430UserNotFoundException, Status444UserIsNull;
    Post getById(String postId) throws Status440PostNotFoundException;
    void deleteById(String postId, JwtUser jwtUser) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, Status444UserIsNull;
    Post save(Post post);
    Post updatePost(PostDTO postDTO, String postId, JwtUser jwtUser) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, IOException, Status443FileIsNullException, Status444UserIsNull, Status445PictureIsNull;
    boolean userIsPostAuthor(String postId, JwtUser jwtUser) throws Exception;

    List<Post> getAllByAuthenticated(JwtUser jwtUser) throws Exception;

    List<Comment> getCommentsByPostId(String postId) throws Status440PostNotFoundException;
    boolean existsById(String postId);


    List<Post> getAllByUsername(String username) throws Status444UserIsNull;


}

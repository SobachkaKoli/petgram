package com.example.petgram.service;

import com.example.petgram.DTO.CommentDTO;
import com.example.petgram.Exception.*;
import com.example.petgram.model.Comment;
import com.example.petgram.security.jwt.UserPrincipal;

public interface CommentService {


    Comment addComment(CommentDTO commentDTO, String postId, UserPrincipal userPrincipal) throws Status440PostNotFoundException, Status444UserIsNull, Status430UserNotFoundException;

    void deleteComment(String commentId, UserPrincipal userPrincipal) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException, Status444UserIsNull, Status430UserNotFoundException;

    boolean userIsCommentAuthor(String commentId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status430UserNotFoundException;
    Comment save(Comment comment);
    boolean existsById(String id);
    void deleteAllByPostId(String postId);
    Comment getById(String commentId);
}

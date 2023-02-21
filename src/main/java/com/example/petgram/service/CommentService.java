package com.example.petgram.service;

import com.example.petgram.DTO.CommentDTO;
import com.example.petgram.Exception.Status436UserNotCommentAuthorException;
import com.example.petgram.Exception.Status439CommentNotFoundException;
import com.example.petgram.Exception.Status440PostNotFoundException;
import com.example.petgram.Exception.Status444UserIsNull;
import com.example.petgram.model.Comment;
import com.example.petgram.security.JwtUser;

public interface CommentService {


    Comment addComment(CommentDTO commentDTO, String postId, JwtUser jwtUser) throws Status440PostNotFoundException, Status444UserIsNull;

    void deleteComment(String commentId, JwtUser jwtUser) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException, Status444UserIsNull;

    boolean userIsCommentAuthor(String commentId, JwtUser jwtUser) throws Status444UserIsNull;
    Comment save(Comment comment);
    boolean existsById(String id);
    void deleteAllByPostId(String postId);
    Comment getById(String commentId);
}

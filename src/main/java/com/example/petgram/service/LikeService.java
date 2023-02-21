package com.example.petgram.service;

import com.example.petgram.Exception.*;
import com.example.petgram.security.JwtUser;

public interface LikeService {
    void likePost(String postId, JwtUser jwtUser) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException;
    void unLikePost(String postId,JwtUser jwtUser ) throws Status444UserIsNull, Status440PostNotFoundException, Status438LikeNotFoundException;

    void likeComment(String commentId,JwtUser jwtUser ) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException, Status439CommentNotFoundException;
    void unLikeComment(String commentId,JwtUser jwtUser ) throws Status444UserIsNull, Status440PostNotFoundException, Status439CommentNotFoundException, Status438LikeNotFoundException;
    void deleteAllByDocumentId(String documentId);
}

package com.example.petgram.service;

import com.example.petgram.Exception.*;
import com.example.petgram.security.jwt.UserPrincipal;

public interface LikeService {
    void likePost(String postId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException, Status430UserNotFoundException;
    void unLikePost(String postId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status440PostNotFoundException, Status438LikeNotFoundException, Status430UserNotFoundException;

    void likeComment(String commentId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException, Status439CommentNotFoundException, Status430UserNotFoundException;
    void unLikeComment(String commentId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status440PostNotFoundException, Status439CommentNotFoundException, Status438LikeNotFoundException, Status430UserNotFoundException;
    void deleteAllByDocumentId(String documentId);
}

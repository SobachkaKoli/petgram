package com.example.petgram.controller;

import com.example.petgram.Exception.*;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.CommentService;
import com.example.petgram.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class CommentController {

    private final CommentService commentService;
    private final LikeService likeService;
    @Autowired
    public CommentController(CommentService commentService,LikeService likeService) {
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public void deleteComment(@PathVariable String commentId, JwtUser jwtUser) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException, Status444UserIsNull {
        commentService.deleteComment(commentId, jwtUser);
    }
    
    @PostMapping("/comment/set-like/{commentId}")
    public void setLikeToComment(@PathVariable String commentId, JwtUser jwtUser) throws Status437LikeAlreadyExistsException, Status439CommentNotFoundException, Status444UserIsNull, Status440PostNotFoundException {
        likeService.likeComment(commentId,jwtUser);
    }

    @DeleteMapping("/comment/unset-like/{commentId}")
    public void unsetLikeToComment(@PathVariable String commentId, JwtUser jwtUser) throws Status438LikeNotFoundException, Status439CommentNotFoundException, Status444UserIsNull, Status440PostNotFoundException {
        likeService.unLikeComment(commentId,jwtUser);
    }

}

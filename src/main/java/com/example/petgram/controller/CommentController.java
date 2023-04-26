package com.example.petgram.controller;

import com.example.petgram.Exception.*;
import com.example.petgram.security.jwt.UserPrincipal;
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
    //  TODO (Bogdan O.) 26/4/23: use proper CRUD namings
    @DeleteMapping("/comment/delete/{commentId}")
    public void deleteComment(@PathVariable String commentId, UserPrincipal userPrincipal) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException, Status444UserIsNull, Status430UserNotFoundException {
        commentService.deleteComment(commentId, userPrincipal);
    }

    //  TODO (Bogdan O.) 26/4/23: use proper CRUD namings
    @PostMapping("/comment/set-like/{commentId}")
    public void setLikeToComment(@PathVariable String commentId, UserPrincipal userPrincipal) throws Status437LikeAlreadyExistsException, Status439CommentNotFoundException, Status444UserIsNull, Status440PostNotFoundException, Status430UserNotFoundException {
        likeService.likeComment(commentId, userPrincipal);
    }

    //  TODO (Bogdan O.) 26/4/23: use proper CRUD namings
    @DeleteMapping("/comment/unset-like/{commentId}")
    public void unsetLikeToComment(@PathVariable String commentId, UserPrincipal userPrincipal) throws Status438LikeNotFoundException, Status439CommentNotFoundException, Status444UserIsNull, Status440PostNotFoundException, Status430UserNotFoundException {
        likeService.unLikeComment(commentId, userPrincipal);
    }

}

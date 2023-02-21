package com.example.petgram.service.serviceImpl;

import com.example.petgram.Exception.*;
import com.example.petgram.model.Comment;
import com.example.petgram.model.ContentType;
import com.example.petgram.model.Like;
import com.example.petgram.model.Post;
import com.example.petgram.repository.LikeRepository;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.*;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final NotificationService notificationService;
    public LikeServiceImpl(LikeRepository likeRepository, UserService userService, PostService postService, CommentService commentService, NotificationService notificationService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.notificationService = notificationService;
    }


    @Override
    public void likePost(String postId, JwtUser jwtUser) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException {
        if (!postService.existsById(postId)) {
            throw new Status440PostNotFoundException(postId);
        }else {
            if (likeRepository.existsByDocumentIdAndAuthor(postId, userService.getAuthenticatedUser(jwtUser))) {
                throw new Status437LikeAlreadyExistsException("you already liked post with id: " + postId);
            } else {
                Post post = postService.getById(postId);
                likeRepository.save(Like.builder()
                    .author(userService.getAuthenticatedUser(jwtUser))
                    .contentType(ContentType.POST)
                    .documentId(postId)
                    .build());
                notificationService.sendNotification(jwtUser,
                        postService.getById(postId).getAuthor().getUserName(),
                        "New like to the post from " + userService.getAuthenticatedUser(jwtUser).getUserName(),postId,ContentType.POST);

                post.setCountLikes(likeRepository.countLikeByDocumentId(postId));
                postService.save(post);
            }
        }
    }

    @Override
    public void unLikePost(String postId, JwtUser jwtUser) throws Status444UserIsNull, Status440PostNotFoundException, Status438LikeNotFoundException {
        if (!postService.existsById(postId)) {
            throw new Status440PostNotFoundException(postId);
        } else {
            if (likeRepository.existsByDocumentIdAndAuthor(postId, userService.getAuthenticatedUser(jwtUser))) {
                likeRepository.deleteLikeByDocumentIdAndAuthor(postId, userService.getAuthenticatedUser(jwtUser));
                Post post = postService.getById(postId);
                post.setCountLikes(likeRepository.countLikeByDocumentId(postId));
                postService.save(post);
            } else {
                throw new Status438LikeNotFoundException("post with id :" + postId);
            }
        }
    }

    @Override
    public void likeComment(String commentId, JwtUser jwtUser) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException, Status439CommentNotFoundException {
        if (!commentService.existsById(commentId)) {
            throw new Status439CommentNotFoundException(commentId);
        }else {
            if (likeRepository.existsByDocumentIdAndAuthor(commentId, userService.getAuthenticatedUser(jwtUser))) {
                throw new Status437LikeAlreadyExistsException("you already liked comment with id: " + commentId);
            } else {
                Comment comment = commentService.getById(commentId);
                likeRepository.save(Like.builder()
                        .author(userService.getAuthenticatedUser(jwtUser))
                        .contentType(ContentType.COMMENT)
                        .documentId(commentId)
                        .build());
                notificationService.sendNotification(jwtUser,
                        postService.getById(commentId).getAuthor().getUserName(),
                        "New like to the comment from " + userService.getAuthenticatedUser(jwtUser).getUserName(),commentId,ContentType.COMMENT);
                comment.setCountLikes(likeRepository.countLikeByDocumentId(commentId));
                commentService.save(comment);
            }
        }
    }

    @Override
    public void unLikeComment(String commentId, JwtUser jwtUser) throws Status444UserIsNull,Status439CommentNotFoundException, Status438LikeNotFoundException {
        if (!commentService.existsById(commentId)) {
            throw new Status439CommentNotFoundException(commentId);
        } else {
            if (likeRepository.existsByDocumentIdAndAuthor(commentId, userService.getAuthenticatedUser(jwtUser))) {
                likeRepository.deleteLikeByDocumentIdAndAuthor(commentId, userService.getAuthenticatedUser(jwtUser));
                Comment comment = commentService.getById(commentId);
                comment.setCountLikes(likeRepository.countLikeByDocumentId(commentId));
                commentService.save(comment);
            } else {
                throw new Status438LikeNotFoundException("comment with id :" + commentId);
            }
        }
    }

    @Override
    public void deleteAllByDocumentId(String documentId) {
        likeRepository.deleteAllByDocumentId(documentId);
    }

}

package com.example.petgram.service.serviceImpl;

import com.example.petgram.Exception.*;
import com.example.petgram.model.Comment;
import com.example.petgram.model.ContentType;
import com.example.petgram.model.Like;
import com.example.petgram.model.Post;
import com.example.petgram.repository.LikeRepository;
import com.example.petgram.security.jwt.UserPrincipal;
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
    public void likePost(String postId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException, Status430UserNotFoundException {
        if (!postService.existsById(postId)) {
            throw new Status440PostNotFoundException(postId);
        }else {
            if (likeRepository.existsByDocumentIdAndAuthor(postId, userService.getAuthenticatedUser(userPrincipal))) {
                throw new Status437LikeAlreadyExistsException("you already liked post with id: " + postId);
            } else {
                Post post = postService.getById(postId);
                likeRepository.save(Like.builder()
                    .author(userService.getAuthenticatedUser(userPrincipal))
                    .contentType(ContentType.POST)
                    .documentId(postId)
                    .build());
                notificationService.sendNotification(userPrincipal,
                        postService.getById(postId).getAuthor().getUsername(),
                        "New like to the post from " + userService.getAuthenticatedUser(userPrincipal).getUsername(),postId,ContentType.POST);

                post.setCountLikes(likeRepository.countLikeByDocumentId(postId));
                postService.save(post);
            }
        }
    }

    @Override
    public void unLikePost(String postId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status440PostNotFoundException, Status438LikeNotFoundException, Status430UserNotFoundException {
        if (!postService.existsById(postId)) {
            throw new Status440PostNotFoundException(postId);
        } else {
            if (likeRepository.existsByDocumentIdAndAuthor(postId, userService.getAuthenticatedUser(userPrincipal))) {
                likeRepository.deleteLikeByDocumentIdAndAuthor(postId, userService.getAuthenticatedUser(userPrincipal));
                Post post = postService.getById(postId);
                post.setCountLikes(likeRepository.countLikeByDocumentId(postId));
                postService.save(post);
            } else {
                throw new Status438LikeNotFoundException("post with id :" + postId);
            }
        }
    }

    @Override
    public void likeComment(String commentId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status440PostNotFoundException, Status437LikeAlreadyExistsException, Status439CommentNotFoundException, Status430UserNotFoundException {
        if (!commentService.existsById(commentId)) {
            throw new Status439CommentNotFoundException(commentId);
        }else {
            if (likeRepository.existsByDocumentIdAndAuthor(commentId, userService.getAuthenticatedUser(userPrincipal))) {
                throw new Status437LikeAlreadyExistsException("you already liked comment with id: " + commentId);
            } else {
                Comment comment = commentService.getById(commentId);
                likeRepository.save(Like.builder()
                        .author(userService.getAuthenticatedUser(userPrincipal))
                        .contentType(ContentType.COMMENT)
                        .documentId(commentId)
                        .build());
                notificationService.sendNotification(userPrincipal,
                        postService.getById(commentId).getAuthor().getUsername(),
                        "New like to the comment from " + userService.getAuthenticatedUser(userPrincipal).getUsername(),commentId,ContentType.COMMENT);
                comment.setCountLikes(likeRepository.countLikeByDocumentId(commentId));
                commentService.save(comment);
            }
        }
    }

    @Override
    public void unLikeComment(String commentId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status439CommentNotFoundException, Status438LikeNotFoundException, Status430UserNotFoundException {
        if (!commentService.existsById(commentId)) {
            throw new Status439CommentNotFoundException(commentId);
        } else {
            if (likeRepository.existsByDocumentIdAndAuthor(commentId, userService.getAuthenticatedUser(userPrincipal))) {
                likeRepository.deleteLikeByDocumentIdAndAuthor(commentId, userService.getAuthenticatedUser(userPrincipal));
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

package com.example.petgram.service.serviceImpl;

import com.example.petgram.DTO.CommentDTO;
import com.example.petgram.Exception.*;
import com.example.petgram.model.ContentType;
import com.example.petgram.model.Role;
import com.example.petgram.model.Comment;
import com.example.petgram.model.Post;
import com.example.petgram.repository.CommentRepository;
import com.example.petgram.repository.PostRepository;
import com.example.petgram.security.jwt.UserPrincipal;
import com.example.petgram.service.CommentService;
import com.example.petgram.service.NotificationService;
import com.example.petgram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;


    public CommentServiceImpl(PostRepository postRepository, UserService userService, CommentRepository commentRepository, NotificationService notificationService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Comment addComment(CommentDTO commentDTO, String postId, UserPrincipal userPrincipal) throws Status440PostNotFoundException, Status444UserIsNull, Status430UserNotFoundException {
        if(postRepository.existsById(postId)) {
            Post post = postRepository.findById(postId).orElseThrow(()-> new Status440PostNotFoundException(postId));

            log.info("Post {} is found", post);

            Comment comment = Comment.builder()
                    .text(commentDTO.getText())
                    .author(userService.getAuthenticatedUser(userPrincipal))
                    .contentType(ContentType.COMMENT)
                    .post(post)
                    .build();

            post.getComments().add(comment);
            commentRepository.save(comment);

            log.info("Comment {} added", comment);

            notificationService.sendNotification(userPrincipal,
                    post.getAuthor().getUsername(),
                    "New comment to the post from " + userService.getAuthenticatedUser(userPrincipal).getUsername(),postId,ContentType.COMMENT);
            post.setCountComment(post.getCountComment() + 1);
            log.info("Notification  sent to {} ",post.getAuthor());
            postRepository.save(post);
            log.info("Post {} is saved", post);
            return comment;
        }else{
            throw new Status440PostNotFoundException(postId);
        }
    }

    @Override
    public void deleteComment(String commentId, UserPrincipal userPrincipal) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException, Status444UserIsNull, Status430UserNotFoundException {
        if (commentRepository.existsById(commentId)) {
            log.info("comment with id {} exists", commentId);
            if (userIsCommentAuthor(commentId, userPrincipal) | userService.getAuthenticatedUser(userPrincipal).getRole().equals(Role.ADMIN)) {
                log.info("comment is author {} of comment", userPrincipal.getUsername());
                commentRepository.deleteById(commentId);
            } else {
                throw new Status436UserNotCommentAuthorException(commentId);
            }
        }else {
            throw new Status439CommentNotFoundException(commentId);
        }
    }

    @Override
    public boolean userIsCommentAuthor(String commentId, UserPrincipal userPrincipal) throws Status444UserIsNull, Status430UserNotFoundException {
        String userId = userService.getAuthenticatedUser(userPrincipal).getId();
        log.info("User id {} ", userId);
        //  TODO (Bogdan O.) 26/4/23: use custom Exceptions instead of .get()
        String authorId = commentRepository.findById(commentId).get().getAuthor().getId();
        log.info("Author id {}", authorId);
        return authorId.equals(userId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public boolean existsById(String commentId) {
        return commentRepository.existsById( commentId);
    }

//    @Override
//    public List<Comment> getPostComments(String postId) {
//        return commentRepository.getCommentsByPostId(postId);
//    }j1

    @Override
    public void deleteAllByPostId(String postId) {
        commentRepository.deleteAllByPostId(postId);
    }
    @Override
    public Comment getById(String commentId) {
        return commentRepository.getCommentById(commentId);
    }
}

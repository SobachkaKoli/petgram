package com.example.petgram.controller;

import com.example.petgram.DTO.CommentDTO;
import com.example.petgram.DTO.PostDTO;
import com.example.petgram.Exception.*;
import com.example.petgram.model.Comment;
import com.example.petgram.model.Post;
import com.example.petgram.model.SponsorPost;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.CommentService;
import com.example.petgram.service.LikeService;
import com.example.petgram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class PostController {
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;
    @Autowired
    public PostController(PostService postService, LikeService likeService, CommentService commentService) {
        this.postService = postService;
        this.likeService = likeService;
        this.commentService = commentService;
    }



    @GetMapping("/get-my-posts")
    private List<Post> getPostsAuthenticatedUser(JwtUser jwtUser) throws Exception {
        return postService.getAllByAuthenticated(jwtUser);}

    @GetMapping("/get-user-posts/{username}")
    private List<Post> getPostsUser(@PathVariable String username) throws Status444UserIsNull {
        return postService.getAllByUsername(username);}

    @GetMapping("/get-post-comments/{postId}")
    private List<Comment> getPostComments(@PathVariable String postId) throws Status440PostNotFoundException {
        return postService.getCommentsByPostId(postId);
    }

    @PostMapping("/create-post")
    private Post createPost(@ModelAttribute PostDTO postDTO, JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status444UserIsNull {
        return postService.createPost(postDTO,jwtUser);
    }


    @PostMapping("/create-sponsor-post/{sponsorName}")
    private SponsorPost createSponsorPost(@ModelAttribute PostDTO postDTO,
                                                          JwtUser jwtUser,
                                                          @PathVariable String sponsorName) throws IOException, Status443FileIsNullException, Status430UserNotFoundException, Status444UserIsNull {
        return postService.createSponsorPost(postDTO,sponsorName,jwtUser);
    }

    @PatchMapping("/update-post/{postId}")
    public Post updatePost(@ModelAttribute PostDTO postDTO,@PathVariable String postId,JwtUser jwtUser)
            throws Status435UserNotPostAuthorException, Status440PostNotFoundException, IOException, Status443FileIsNullException, Status444UserIsNull, Status445PictureIsNull {
        return postService.updatePost(postDTO,postId,jwtUser);
    }

    @PostMapping("/post/like/{postId}")
    private void setLikeToPost(@PathVariable String postId, JwtUser jwtUser)
            throws Status437LikeAlreadyExistsException, Status440PostNotFoundException, Status444UserIsNull {
        likeService.likePost(postId,jwtUser);
    }

    @DeleteMapping("/post/unlike/{postId}")
    public void unsetLikeToPost(@PathVariable String postId, JwtUser jwtUser) throws Status438LikeNotFoundException, Status440PostNotFoundException, Status444UserIsNull {
        likeService.unLikePost(postId, jwtUser);
    }

    @PostMapping("/post/addComment/{postId}")
    public Comment addComment(@RequestBody CommentDTO commentDTO,
                                              @PathVariable String postId,
                                              JwtUser jwtUser) throws Status440PostNotFoundException, Status444UserIsNull {
        return commentService.addComment(commentDTO,postId,jwtUser);

    }

    @DeleteMapping("/post/delete/{id}")
    private void deletePost(@PathVariable String id, JwtUser jwtUser) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, Status444UserIsNull {
        postService.deleteById(id,jwtUser);
    }

}

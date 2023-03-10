package com.example.petgram.service.serviceImpl;

import com.example.petgram.DTO.PostDTO;
import com.example.petgram.Exception.*;
import com.example.petgram.model.*;
import com.example.petgram.repository.LikeRepository;
import com.example.petgram.repository.PostRepository;
import com.example.petgram.repository.SponsorPostRepository;
import com.example.petgram.security.JwtUser;
import com.example.petgram.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final SponsorPostRepository sponsorPostRepository;
    private final PictureService pictureService;
    private final LikeRepository likeRepository;


    public PostServiceImpl(PostRepository postRepository, UserService userService, CommentService commentService, SponsorPostRepository sponsorPostRepository, PictureService pictureService, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.sponsorPostRepository = sponsorPostRepository;
        this.pictureService = pictureService;
        this.likeRepository = likeRepository;

    }

    @Override
    public Post createPost(PostDTO postDTO, JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status444UserIsNull {

        List<String> picturePath = new ArrayList<>();
        for (MultipartFile file : postDTO.getPictures()){
                picturePath.add(pictureService.savePicture(
                        file,pictureService.createPostPicturePath(jwtUser)));
        }


        //userService.getAuthenticatedUser(jwtUser) -> jwtuser
        return postRepository.save(Post.builder()
                .author(jwtUser.getUser())
                .text(postDTO.getText())
                .picturePath(picturePath)
                .contentType(ContentType.POST)
                .comments(new ArrayList<>())
                .build());
    }

    @Override
    public SponsorPost createSponsorPost(PostDTO postDTO,String sponsorUsername, JwtUser jwtUser) throws IOException, Status443FileIsNullException, Status430UserNotFoundException, Status444UserIsNull {
        if (userService.existsByUsername(sponsorUsername)){
            return sponsorPostRepository.save(SponsorPost.builder()
                    .post(createPost(postDTO,jwtUser))
                    .sponsor(userService.getByUserName(sponsorUsername))
                    .build());
        }else {
            throw new Status430UserNotFoundException(sponsorUsername);
        }
    }

    @Override
    public Post getById(String postId) throws Status440PostNotFoundException {
        return postRepository.findById(postId).orElseThrow(() -> new Status440PostNotFoundException(postId));
    }

    @Override
    public void deleteById(String postId, JwtUser jwtUser) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, Status444UserIsNull {
        if(postRepository.existsById(postId)) {
            if (userIsPostAuthor(postId, jwtUser) | userService.getAuthenticatedUser(jwtUser).getRole().equals(Role.ADMIN)) {
                Optional<Post> post = postRepository.findById(postId);
                for (String picturePath : post.get().getPicturePath()){
                    File picture = new File(picturePath);
                    picture.delete();
                }
                likeRepository.deleteAllByDocumentId(postId);
                commentService.deleteAllByPostId(postId);
                postRepository.deleteById(postId);
            } else {
                throw new Status435UserNotPostAuthorException(postId);
            }
        }else {
            throw new Status440PostNotFoundException(postId);
        }
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(PostDTO postDTO, String postId, JwtUser jwtUser)throws Status435UserNotPostAuthorException, Status440PostNotFoundException, IOException, Status443FileIsNullException, Status444UserIsNull{
        if (!postRepository.existsById(postId)) {
            throw new Status440PostNotFoundException(postId);
        } else {
            if (!userIsPostAuthor(postId, jwtUser)) {
                throw new Status435UserNotPostAuthorException(postId);
            }else {
                Post post = postRepository.findById(postId).orElseThrow();

                if (!postDTO.getPictures().isEmpty()) {
                    for (String picturePath : post.getPicturePath()) {
                        File picture = new File(picturePath);
                        picture.delete();
                    }
                    post.getPicturePath().clear();
                    for (MultipartFile picture : postDTO.getPictures()) {
                        post.getPicturePath().add(pictureService.savePicture(picture, pictureService.createPostPicturePath(jwtUser)));
                    }
                }

                if (!postDTO.getText().isEmpty()) {
                    post.setText(postDTO.getText());
                }
                return postRepository.save(post);
            }
        }
    }

    @Override
    public boolean userIsPostAuthor(String postId, JwtUser jwtUser) throws Status444UserIsNull, Status440PostNotFoundException {
        String userId = userService.getAuthenticatedUser(jwtUser).getId();
        String authorId = postRepository.findById(postId).orElseThrow(() -> new Status440PostNotFoundException(postId)).getAuthor().getId();
        return authorId.equals(userId);
    }


    @Override
    public List<Post> getAllByAuthenticated(JwtUser jwtUser) throws Status444UserIsNull {
        return postRepository.findAllByAuthor(jwtUser.getUser());
    }
    @Override
    public List<Comment> getCommentsByPostId(String postId) throws Status440PostNotFoundException {
        return postRepository.findById(postId).orElseThrow(() -> new Status440PostNotFoundException(postId)).getComments();
    }

    @Override
    public boolean existsById(String postId) {
        return postRepository.existsById(postId);
    }

    @Override
    public List<Post> getAllByUsername(String username) throws Status444UserIsNull {
        return postRepository.findAllByAuthor(userService.getByUserName(username));
    }


}

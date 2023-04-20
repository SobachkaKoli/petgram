package com.example.petgram.repository;

import com.example.petgram.model.Comment;
import com.example.petgram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {


    Comment getCommentById(String id);

    void deleteCommentById(String id);

    void deleteAllByPostId(String postId);



}

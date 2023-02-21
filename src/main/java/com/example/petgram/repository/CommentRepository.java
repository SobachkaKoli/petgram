package com.example.petgram.repository;

import com.example.petgram.model.Comment;
import com.example.petgram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {

    List<Comment> findAllByAuthor(User author);
    Comment getCommentById(String id);

    void deleteAllByPostId(String postId);

   List<Comment> getCommentsByPostId(String postId);

}

package com.example.petgram.repository;

import com.example.petgram.model.Post;
import com.example.petgram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {





    List<Post> findAllByAuthorId(String authorId);

    List<Post> findAllByAuthor(User user);

    List<Post> findAll();

}

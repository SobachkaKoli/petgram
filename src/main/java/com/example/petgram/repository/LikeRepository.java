package com.example.petgram.repository;

import com.example.petgram.model.Like;
import com.example.petgram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends MongoRepository<Like,String> {
    boolean existsByDocumentIdAndAuthor(String documentId, User user);
    int countLikeByDocumentId(String documentId);
    void deleteLikeByDocumentIdAndAuthor(String documentId, User author);
    void deleteAllByDocumentId(String documentId);

}

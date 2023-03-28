package com.example.petgram.repository;

import com.example.petgram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findByUsername(String userName);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    boolean existsByUsername(String userName);

//    void deleteByUsername(String userName);
}

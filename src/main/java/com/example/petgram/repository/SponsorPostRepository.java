package com.example.petgram.repository;

import com.example.petgram.model.SponsorPost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SponsorPostRepository extends MongoRepository<SponsorPost,String> {
}

package com.razz.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.razz.model.Event;
import com.razz.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

}

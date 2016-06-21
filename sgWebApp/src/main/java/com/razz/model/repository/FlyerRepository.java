package com.razz.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.razz.model.Flyer;

public interface FlyerRepository extends MongoRepository<Flyer, String> {
	
}

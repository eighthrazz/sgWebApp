package com.razz.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.razz.model.Venue;

public interface VenueRepository extends MongoRepository<Venue, String> {

	public Venue findByName(String name);
	
}

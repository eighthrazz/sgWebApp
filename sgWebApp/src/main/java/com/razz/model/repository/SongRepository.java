package com.razz.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.razz.model.Song;

public interface SongRepository extends MongoRepository<Song, String> {

	public Song findByName(String name);
	
}

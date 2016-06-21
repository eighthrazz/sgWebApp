package com.razz.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.razz.model.Song;
import com.razz.model.SongStat;

public interface SongStatsRepository extends MongoRepository<SongStat, String> {

	public SongStat findBySong(Song song);
	
}

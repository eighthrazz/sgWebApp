package com.razz.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.razz.model.Event;

public interface EventRepository extends MongoRepository<Event, String> {

}

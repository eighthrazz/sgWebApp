package com.razz.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.razz.model.Event;

public interface CalendarEventRepository extends MongoRepository<Event, String> {

}

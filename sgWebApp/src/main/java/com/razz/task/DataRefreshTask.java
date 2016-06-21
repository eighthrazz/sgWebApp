package com.razz.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.razz.model.Event;
import com.razz.model.Post;
import com.razz.model.repository.CalendarEventRepository;
import com.razz.model.repository.PostRepository;
import com.razz.service.FacebookService;
import com.razz.service.GoogleCalendarService;
import com.razz.service.LoggerService;
import com.razz.service.TwitterService;

@Component
public class DataRefreshTask {

	@Autowired
	private LoggerService logger;

	@Autowired
	private FacebookService faceBookService;

	@Autowired
	private TwitterService twitterService;

	@Autowired
	private GoogleCalendarService googleCalendarService;

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CalendarEventRepository calendarEventRepository;

	@Scheduled(fixedRate=60000, initialDelay=15000)
	public void fetchFacebookPosts() {
		//
		logger.debug(getClass(), "retreiving Facebook posts...");
		final List<Post> fbPostList = faceBookService.getPostList();
		final int size = fbPostList.size();
		
		//
		savePostList(fbPostList);
		logger.debug(getClass(), "fetched %s Facebook post(s), saved %s post(s)", 
				size, fbPostList.size());
	}

	@Scheduled(fixedRate=60000, initialDelay=30000)
	public void fetchTwitterPosts() {
		//
		logger.debug(getClass(), "retreiving Twitter tweets...");
		final List<Post> twPostList = twitterService.getPostList();
		final int size = twPostList.size();
		
		//
		savePostList(twPostList);
		logger.debug(getClass(), "fetched %s Twitter tweet(s), saved %s tweet(s)", 
				size, twPostList.size());
	}

	@Scheduled(fixedRate=60000, initialDelay=5000) //TODO was 45000
	public void fetchGoogleCalendarEvents() {
		//
		logger.debug(getClass(), "retreiving Goolge Calendar events...");
		final List<Event> eventList = new ArrayList<>();
		try {
			eventList.addAll( googleCalendarService.getEventsStartingToday() );
		}
		catch(Exception e) {
			logger.error(getClass(), e, "Unable to retrieve Google Calendar events.");
		}
		final int size = eventList.size();
		
		//
		saveEventList(eventList);
		logger.debug(getClass(), "fetched %s Google Calendar event(s), saved %s event(s)", 
				size, eventList.size());
	}
	
	void savePostList(List<Post> postList) {
		final Iterator<Post> postIter = postList.iterator();
		while (postIter.hasNext()) {
			final Post post = postIter.next();
			if (postRepository.findOne(post.getId()) != null)
				postIter.remove();
			else
				postRepository.save(post);
		}
	}
	
	void saveEventList(List<Event> eventList) {
		final Iterator<Event> eventIter = eventList.iterator();
		while (eventIter.hasNext()) {
			final Event event = eventIter.next();
			if (calendarEventRepository.findOne(event.getId()) != null)
				eventIter.remove();
			else
				calendarEventRepository.save(event);
		}
	}
	
}

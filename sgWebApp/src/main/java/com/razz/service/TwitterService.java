package com.razz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import com.razz.model.Post.Type;
import com.razz.util.Patterns;

@Service("TwitterService")
public class TwitterService {

	@Autowired
	private LoggerService logger;
	
	@Value("${spring.social.twitter.app-id}")
	private String appId;
	
	@Value("${spring.social.twitter.app-secret}")
	private String appSecret;
	
	public List<com.razz.model.Post> getPostList() {
		final Twitter twitter = new TwitterTemplate(appId, appSecret);
		final TimelineOperations timeLineOper = twitter.timelineOperations();
		final List<Tweet> tweetList = 
				timeLineOper.getUserTimeline("SatchelGrande");
		final List<com.razz.model.Post> twPostList = new ArrayList<>(
				tweetList != null ? tweetList.size() : 0);
		for(Tweet t : tweetList) {
    		final com.razz.model.Post twPost = getTwitterPost(t);
    		twPostList.add(twPost);
    	}
    	return twPostList;
    }
	
	com.razz.model.Post getTwitterPost(Tweet tweet) {
		logger.debug(getClass(), "%n[unmodifiedtext]%n%s%n", tweet.getUnmodifiedText());
		
		final String id = tweet.getIdStr();
		final Date date = tweet.getCreatedAt();
		final String picture = "";
		
		final com.razz.model.Post twPost = new com.razz.model.Post();
		twPost.setId(id);
		twPost.setType(Type.twitter);
		twPost.setDate( date.getTime() );
		twPost.setImageUrl(picture);
		fillMessageAndLink(tweet.getUnmodifiedText(), twPost);
		logger.debug(getClass(), "%s", twPost);
		return twPost;
	}
	
	void fillMessageAndLink(String message, com.razz.model.Post post) {
		int start = -1;
		int end = -1;
		final Matcher matcher = Patterns.WEB_URL.matcher(message);
		while( matcher.find() ) {
			start = matcher.start();
			end = matcher.end();
		}
		post.setMessage( message.substring(0, start) );
		post.setLink( message.substring(start, end) );
	}
	
}

package com.razz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.stereotype.Service;

import com.razz.model.Post.Type;

@Service("FacebookService")
public class FacebookService {

	@Autowired
	private LoggerService logger;
	
	@Value("${spring.social.facebook.app-id}")
	private String appId;
	
	@Value("${spring.social.facebook.app-secret}")
	private String appSecret;
	
	public List<com.razz.model.Post> getPostList() {
		final String appToken = fetchApplicationAccessToken();
        final Facebook facebook = new FacebookTemplate(appToken);
        final PagedList<Post> pagedList = facebook.feedOperations().getFeed("sgomaha");
        final List<com.razz.model.Post> fbPostList = new ArrayList<>(
        		pagedList != null ? pagedList.size() : 0);
    	for(Post p : pagedList) {
    		final com.razz.model.Post fbPost = getFacebookPost(p);
    		fbPostList.add(fbPost);
    	}
    	return fbPostList;
    }
	
	com.razz.model.Post getFacebookPost(Post post) {
		final String id = post.getId();
		final Reference reference = post.getFrom();
		final String name = reference.getName();
		final Date date = post.getCreatedTime();
		final String message = getMessage(post);
		final String link = post.getLink();
		final String picture = post.getPicture();
		logger.debug(getClass(), 
				"id=%s, name=%s, date=%s, message=%s, link=%s, picture=%s, type=%s, " +
				"description=%s, statusType=%s, story=%s", 
				id, name, date, message, link, picture, post.getType(), 
				post.getDescription(), post.getStatusType(), post.getStory());
		final com.razz.model.Post fbPost = new com.razz.model.Post();
		fbPost.setId(id);
		fbPost.setType(Type.facebook);
		fbPost.setDate( date.getTime() );
		fbPost.setMessage(message);
		fbPost.setImageUrl(picture);
		fbPost.setLink(link);
		return fbPost;
	}
	
	String getMessage(Post post) {
		if(post.getMessage() != null)
			return post.getMessage();
		if(post.getDescription() != null)
			return post.getDescription();
		if(post.getStory() != null)
			return post.getStory();
		return null;
	}
	
	private String fetchApplicationAccessToken() {
		logger.debug(getClass(), 
				"appId=%s, appSecret=%s", appId, appSecret);
        final OAuth2Operations oauth = new FacebookConnectionFactory(appId, appSecret).getOAuthOperations();
        final String appToken = oauth.authenticateClient().getAccessToken();
        return appToken;
    }
	
}

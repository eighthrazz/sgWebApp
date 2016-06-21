package com.razz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.razz.model.Post;
import com.razz.model.repository.PostRepository;
import com.razz.service.LoggerService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;

@Controller
public class PostController {

	@Autowired
	private LoggerService logger;
	
	@Autowired
	private PostRepository postRepository;
	
	@RequestMapping(value = "/posts/{page}", method = RequestMethod.GET)
    public String posts(@PathVariable("page") int page, Model model) {
		logger.debug(getClass(), "posts: page=%s", page);
		
		//
		model.addAttribute("page", page);
		
		//
    	final List<Post> posts = new ArrayList<>();
    	posts.addAll(postRepository.findAll());
    	
    	// set empty image URLs to null 
    	for(Post p : posts) {
    		if( p.getImageUrl() != null && p.getImageUrl().trim().length() <= 0 )
    			p.setImageUrl(null);
    	}
    	
    	// reverse order by date
    	Collections.sort(posts, new Comparator<Post>() {
			public int compare(Post o1, Post o2) {
				return ((Long)o2.getDate()).compareTo(o1.getDate());
			}
		});
    	
    	//
    	if(page < 0) {
    		logger.debug(getClass(), "posts: posts.size=%s", posts.size());
			model.addAttribute("posts", posts);
		}
    	
    	//
		else {
			//
			final PagedListHolder<Post> postPageListHolder = new PagedListHolder<>(posts);
			postPageListHolder.setPageSize(4);
			postPageListHolder.setPage(page);
			
			//
			logger.debug(getClass(), "posts: posts.size=%s", postPageListHolder.getPageList().size());
			model.addAttribute("posts", postPageListHolder.getPageList());
			
			//
			model.addAttribute("nextPage", page+1);
			model.addAttribute("lastPage", postPageListHolder.getPageCount());
		}
    	
        return "posts :: resultList";
    }
    
}

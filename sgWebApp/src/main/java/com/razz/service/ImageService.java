package com.razz.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

	@Autowired
	private LoggerService logger;
	
	final static Random random = new Random(System.nanoTime());
	
	public String getRandomFileName(String classpath, String fileExtension) throws Exception {
		final ClassLoader classLoader = this.getClass().getClassLoader(); 
    	final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(classLoader);
    	final String locationPattern = String.format("classpath:%s/*.%s",classpath, fileExtension);
    	final Resource[] resources = resourcePatternResolver.getResources(locationPattern) ;
    	logger.debug(getClass(), "locationPattern=%s, resources.size=%s%n", locationPattern, resources.length); 
    	if(resources == null || resources.length <= 0)
    		return null;
    	final Resource resource = resources[getRandomInt(0, resources.length)];
    	final String filename = resource.getFilename();
    	logger.debug(getClass(), "filename=%s%n", filename);
    	return filename;
	}
	
	public static int getRandomInt(int min, int max) {
		return random.nextInt((max-min) + min);
	}
	
}

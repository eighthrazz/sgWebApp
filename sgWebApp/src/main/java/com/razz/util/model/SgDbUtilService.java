package com.razz.util.model;

import com.razz.util.model.SgDbUtilServiceImpl.LogListener;

public interface SgDbUtilService {

	public boolean isRunning();
	public void run(LogListener logListener) throws Exception;
	
}

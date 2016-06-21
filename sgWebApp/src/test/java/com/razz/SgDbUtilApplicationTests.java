package com.razz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.razz.util.model.SgDbUtilServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SgDbUtilServiceImpl.class)
public class SgDbUtilApplicationTests {

	@Test
	public void contextLoads() {
	}

}

package com.santtuhyvarinen.moviereviews;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.santtuhyvarinen.moviereviews.web.MovieController;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieReviewsApplicationTests {

	@Autowired
	private MovieController controller;
	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

}

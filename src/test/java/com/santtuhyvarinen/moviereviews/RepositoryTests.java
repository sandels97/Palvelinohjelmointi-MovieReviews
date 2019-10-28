package com.santtuhyvarinen.moviereviews;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.santtuhyvarinen.moviereviews.domain.Genre;
import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.domain.Review;
import com.santtuhyvarinen.moviereviews.domain.User;
import com.santtuhyvarinen.moviereviews.interfaces.GenreRepository;
import com.santtuhyvarinen.moviereviews.interfaces.MovieRepository;
import com.santtuhyvarinen.moviereviews.interfaces.ReviewRepository;
import com.santtuhyvarinen.moviereviews.interfaces.UserRepository;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTests {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Test
	public void testRepositories() {
		
		//Test genre repository
		Genre action = genreRepository.save(new Genre("Action"));
		assertThat(genreRepository.findById(action.getId())).isNotNull();
		
		//Test movie repository
		Movie heat = movieRepository.save(new Movie("Heat", 1995, action));
		assertThat(movieRepository.findById(heat.getId())).isNotNull();
		
		//Test user repository
		User user = userRepository.save(new User("testuser","$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6","USER"));
		assertThat(userRepository.findByUsername(user.getUsername())).isNotNull();
		
		//Test review repository
		reviewRepository.save(new Review(heat,user,5));
		Review review = reviewRepository.findByMovieAndUser(heat, user).get(0);
		assertThat(review).isNotNull();
	}
	
	//Test MovieUtil.java methods
	@Test
	public void testMovieUtil() {
		Genre action = genreRepository.save(new Genre("Action"));
		Genre horror = genreRepository.save(new Genre("Horror"));
		Genre drama = genreRepository.save(new Genre("Drama"));
		
		Movie heat = movieRepository.save(new Movie("Heat", 1995, action));
		Movie halloween = movieRepository.save(new Movie("Halloween", 1978, horror));
		Movie cuckoo = movieRepository.save(new Movie("One Flew Over the Cuckoo's Nest", 1975, drama));
		
		User user1 = userRepository.save(new User("testuser1","$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6","USER"));
		User user2 = userRepository.save(new User("testuser2","$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6","USER"));
		User user3 = userRepository.save(new User("testuser3","$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6","USER"));
		
		reviewRepository.save(new Review(heat,user1,5));
		reviewRepository.save(new Review(heat,user2,3));
		
		reviewRepository.save(new Review(halloween,user1,5));
		reviewRepository.save(new Review(halloween,user2,3));
		reviewRepository.save(new Review(halloween,user3,1));
		
		//Test that the calculating average score for movies from the reviews work correctly
		assertThat(MovieUtil.calculateAverageScoreFromReviews(heat, reviewRepository.findByMovie(heat))).isEqualTo(4);
		assertThat(MovieUtil.calculateAverageScoreFromReviews(halloween, reviewRepository.findByMovie(halloween))).isEqualTo(3);
		assertThat(MovieUtil.calculateAverageScoreFromReviews(cuckoo, reviewRepository.findByMovie(cuckoo))).isEqualTo(0);
		
		//Test that the calculating votes for movies from the reviews work correctly
		assertThat(MovieUtil.calculateVotes(heat, reviewRepository.findByMovie(heat))).isEqualTo(2);
		assertThat(MovieUtil.calculateVotes(halloween, reviewRepository.findByMovie(halloween))).isEqualTo(3);
		assertThat(MovieUtil.calculateVotes(cuckoo, reviewRepository.findByMovie(cuckoo))).isEqualTo(0);
	}
	
}

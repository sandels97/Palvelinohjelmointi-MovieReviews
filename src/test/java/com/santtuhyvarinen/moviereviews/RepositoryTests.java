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
		
		Genre action = genreRepository.save(new Genre("Action"));
		Genre horror = genreRepository.save(new Genre("Horror"));
		Genre drama = genreRepository.save(new Genre("Drama"));
		Genre fantasy = genreRepository.save(new Genre("Fantasy"));
		
		List<Genre> genres = (List<Genre>) genreRepository.findAll();
		
		//Test that genre can be found by it's id and name
		assertThat(genreRepository.findById(drama.getId())).isNotNull();
		assertThat(genreRepository.findByName(fantasy.getName())).isNotNull();
		
		Movie heat = movieRepository.save(new Movie("Heat", 1995, action));
		Movie halloween = movieRepository.save(new Movie("Halloween", 1978, horror));
		Movie cuckoo = movieRepository.save(new Movie("One Flew Over the Cuckoo's Nest", 1975, drama));
		Movie lotr = movieRepository.save(new Movie("The Lord of the Rings: The Fellowship of the Ring", 2001, fantasy));
		List<Movie> movies = (List<Movie>) movieRepository.findAll();
		
		//Do the same tests for movie repository
		assertThat(movieRepository.findById(heat.getId())).isNotNull();
		assertThat(movieRepository.findByTitle(cuckoo.getTitle())).isNotNull();
		
		User user = userRepository.save(new User("testuser","$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6","USER"));
		
		reviewRepository.save(new Review(heat,user,5));
		reviewRepository.save(new Review(halloween,user,4));
		reviewRepository.save(new Review(lotr,user,2));

		Review review = reviewRepository.findByMovieAndUser(heat, user).get(0);
		
		//Test that review exists
		assertThat(review).isNotNull();
	}
	
}

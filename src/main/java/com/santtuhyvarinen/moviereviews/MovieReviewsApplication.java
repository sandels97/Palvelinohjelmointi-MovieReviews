package com.santtuhyvarinen.moviereviews;

import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.santtuhyvarinen.moviereviews.domain.Genre;
import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.domain.Review;
import com.santtuhyvarinen.moviereviews.domain.User;
import com.santtuhyvarinen.moviereviews.interfaces.GenreRepository;
import com.santtuhyvarinen.moviereviews.interfaces.MovieRepository;
import com.santtuhyvarinen.moviereviews.interfaces.ReviewRepository;
import com.santtuhyvarinen.moviereviews.interfaces.UserRepository;

@SpringBootApplication
public class MovieReviewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieReviewsApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner bookStoreDemo(MovieRepository movieRepository, 
			GenreRepository genreRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
		return (args) -> {
			Genre action = genreRepository.save(new Genre("Action"));
			Genre horror = genreRepository.save(new Genre("Horror"));
			Genre drama = genreRepository.save(new Genre("Drama"));
			Genre fantasy = genreRepository.save(new Genre("Fantasy"));

			Movie heat = movieRepository.save(new Movie("Heat", 1995, action));
			Movie halloween = movieRepository.save(new Movie("Halloween", 1978, horror));
			Movie cuckoo = movieRepository.save(new Movie("One Flew Over the Cuckoo's Nest", 1975, drama));
			Movie lotr = movieRepository.save(new Movie("The Lord of the Rings: The Fellowship of the Ring", 2001, fantasy));
			
			User user = userRepository.save(new User("user","fsdafsa","USER"));
			User admin = userRepository.save(new User("admin","asdfsfsda","ADMIN"));
			
			Review review1 = reviewRepository.save(new Review(heat,user,5));
			Review review2 = reviewRepository.save(new Review(halloween,user,4));
			Review review3 = reviewRepository.save(new Review(heat,admin,3));
			Review review4 = reviewRepository.save(new Review(lotr,admin,2));
			Review review5 = reviewRepository.save(new Review(halloween,admin,3));
		};
	}
}

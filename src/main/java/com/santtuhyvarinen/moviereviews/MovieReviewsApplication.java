package com.santtuhyvarinen.moviereviews;

import org.springframework.boot.CommandLineRunner;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CommandLineRunner movieReviewsDemo(MovieRepository movieRepository, 
			GenreRepository genreRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
		return (args) -> {
			
			//Add some genres to the database, if there aren't any rows in the genre table
			if(genreRepository.count() == 0) {
				genreRepository.save(new Genre("Action"));
				genreRepository.save(new Genre("Horror"));
				genreRepository.save(new Genre("Drama"));
				genreRepository.save(new Genre("Fantasy"));
				
				genreRepository.save(new Genre("Romance"));
				genreRepository.save(new Genre("Comedy"));
				genreRepository.save(new Genre("Thriller"));
				genreRepository.save(new Genre("Western"));
				genreRepository.save(new Genre("Scifi"));
			}
			
			//Add admin account if one does not exist yet
			if(userRepository.findByUsername("admin") == null) {
				userRepository.save(new User("admin","$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C","ADMIN"));
			}
		};
	}
}

package com.santtuhyvarinen.moviereviews;

import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.santtuhyvarinen.moviereviews.domain.Genre;
import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.interfaces.GenreRepository;
import com.santtuhyvarinen.moviereviews.interfaces.MovieRepository;

@SpringBootApplication
public class MovieReviewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieReviewsApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner bookStoreDemo(MovieRepository movieRepository, GenreRepository genreRepository) {
		return (args) -> {
			Genre action = genreRepository.save(new Genre("Action"));
			Genre horror = genreRepository.save(new Genre("Horror"));
			Genre drama = genreRepository.save(new Genre("Drama"));
			Genre fantasy = genreRepository.save(new Genre("Fantasy"));

			movieRepository.save(new Movie("Heat", 1995, action));
			movieRepository.save(new Movie("Halloween", 1978, horror));
			movieRepository.save(new Movie("One Flew Over the Cuckoo's Nest", 1975, drama));
			movieRepository.save(new Movie("The Lord of the Rings: The Fellowship of the Ring", 2001, fantasy));
		};
	}
}

package com.santtuhyvarinen.moviereviews.interfaces;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.santtuhyvarinen.moviereviews.domain.Movie;

public interface MovieRepository extends CrudRepository<Movie, Long>{
	List<Movie> findByTitle(String title);
	List<Movie> findByReleaseYear(long releaseYear);
}

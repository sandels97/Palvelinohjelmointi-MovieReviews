package com.santtuhyvarinen.moviereviews.interfaces;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.domain.Review;
import com.santtuhyvarinen.moviereviews.domain.User;

public interface ReviewRepository extends CrudRepository<Review, Long>{
	List<Review> findByUser(User user);
	List<Review> findByMovie(Movie movie);
	List<Review> findByMovieAndUser(Movie movie, User user);
}

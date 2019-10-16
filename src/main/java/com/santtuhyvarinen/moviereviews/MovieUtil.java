package com.santtuhyvarinen.moviereviews;

import java.util.List;


import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.domain.Review;

public class MovieUtil {
	public static double calculateAverageScoreFromReviews(Movie movie, List<Review> reviews) {
		
		double sum = 0;
		int reviewCount = 0;
		
		for(Review review : reviews) {
			
			//If the review is for the movie
			if(review.getMovie().getId() == movie.getId()) {
				sum += review.getScore();
				reviewCount ++;
			}
		}

		//Return the average score
		return sum / reviewCount;
	}
	
	public static int calculateVotes(Movie movie, List<Review> reviews) {

		int reviewCount = 0;
		
		for(Review review : reviews) {
			
			//If the review is for the movie, add a vote
			if(review.getMovie().getId() == movie.getId()) {
				reviewCount ++;
			}
		}

		return reviewCount;
	}
}

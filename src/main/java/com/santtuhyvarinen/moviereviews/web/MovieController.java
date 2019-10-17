package com.santtuhyvarinen.moviereviews.web;

import java.io.IOException;

import java.util.Collections;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.domain.Review;
import com.santtuhyvarinen.moviereviews.domain.User;
import com.santtuhyvarinen.moviereviews.interfaces.GenreRepository;
import com.santtuhyvarinen.moviereviews.interfaces.MovieRepository;
import com.santtuhyvarinen.moviereviews.interfaces.ReviewRepository;
import com.santtuhyvarinen.moviereviews.interfaces.UserRepository;
import com.santtuhyvarinen.moviereviews.MovieUtil;

@Controller
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//List of all movies
	@RequestMapping
	public String index(Model model) {

		
		List<Movie> movies = (List<Movie>) movieRepository.findAll();
		List<Review> reviews = (List<Review>) reviewRepository.findAll();

		//Calculate the votes and average score for all the movies
		for(Movie movie : movies) {
			double averageScore = MovieUtil.calculateAverageScoreFromReviews(movie, reviews);
			int votes = MovieUtil.calculateVotes(movie, reviews);
			movie.setAverageScore(averageScore);
			movie.setVotes(votes);
		}
		model.addAttribute("movies", movies);
		return "index";
	}
	
	//Movie's own page
	@RequestMapping(value="/movie/{id}")
	public String moviePage(@PathVariable("id") Long movieId, Model model) {

		Movie movie = movieRepository.findById(movieId).get();;

		//Calculate average score and votes for the movie
		List<Review> reviews = (List<Review>) reviewRepository.findAll();
		double averageScore = MovieUtil.calculateAverageScoreFromReviews(movie, reviews);
		int votes = MovieUtil.calculateVotes(movie, reviews);
		movie.setAverageScore(averageScore);
		movie.setVotes(votes);

		model.addAttribute("movie", movie);
		return "moviepage";
	}
	
	
	//Add a movie -page
	@RequestMapping(value="/add")
	public String addBook(Model model) {
		model.addAttribute("title","Add a movie to the collection");
		model.addAttribute("movie", new Movie());
		model.addAttribute("genres", genreRepository.findAll());
		return	"editmovie";
	}
	
	//Edit movie information
	@RequestMapping(value="/edit/{id}")
	public String editBook(@PathVariable("id") Long movieId, Model model) {
		model.addAttribute("title", "Edit the movie");
		model.addAttribute("movie", movieRepository.findById(movieId));
		model.addAttribute("genres", genreRepository.findAll());
		return "editmovie";
	}
	
	//Leave a review
	@RequestMapping("/leaverating/{username}/{movieid}/{rating}") 
	public String leaveRating(@PathVariable("movieid") Long movieId, @PathVariable("username") String userName, @PathVariable("rating") int rating) {
		System.out.println(movieRepository.findById(movieId).get().getTitle() + ", " + userName + ", " + rating);
		
		Movie movie = movieRepository.findById(movieId).get();
		User user = userRepository.findByUsername(userName);
		
		//Check if user has already left a review for the movie. If the list is empty, user hasn't reviewed the movie yet
		List<Review> reviews = reviewRepository.findByMovieAndUser(movie, user);
		
		if(reviews.size() > 0) {
			//Update previous review
			Review review = reviews.get(0);
			Random r = new Random();
			review.setScore(r.nextInt(5) + 1);
			reviewRepository.save(review);
		} else {
			//Leave a new review
			Review review = new Review(movie, user, rating);
			reviewRepository.save(review);
		}
		
		return "redirect:/movie/" + movieId;
	}
	
	//Save a new movie to database
	@PostMapping("/save") 
	public String save(Movie movie){
		movieRepository.save(movie);
		return "redirect:/index";
	}
	
	//Delete movie from database
	@RequestMapping(value="/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteBook(@PathVariable("id") Long movieId, Model model) {
		movieRepository.deleteById(movieId);
		return "redirect:/index";
	}
}

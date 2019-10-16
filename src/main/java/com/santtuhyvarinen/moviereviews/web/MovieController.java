package com.santtuhyvarinen.moviereviews.web;

import java.util.Collections;

import java.util.Comparator;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.domain.Review;
import com.santtuhyvarinen.moviereviews.interfaces.GenreRepository;
import com.santtuhyvarinen.moviereviews.interfaces.MovieRepository;
import com.santtuhyvarinen.moviereviews.interfaces.ReviewRepository;
import com.santtuhyvarinen.moviereviews.MovieUtil;

@Controller
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@RequestMapping
	public String index(Model model) {
		//List of all movies
		
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
	
	@RequestMapping(value="/add")
	public String addBook(Model model) {
		model.addAttribute("title","Add a movie to the collection");
		model.addAttribute("movie", new Movie());
		model.addAttribute("genres", genreRepository.findAll());
		return	"editmovie";
	}
	
	@RequestMapping(value="/edit/{id}")
	public String editBook(@PathVariable("id") Long movieId, Model model) {
		model.addAttribute("title", "Edit the movie");
		model.addAttribute("movie", movieRepository.findById(movieId));
		model.addAttribute("genres", genreRepository.findAll());
		return "editmovie";
	}
	
	@PostMapping("/save") 
	public String save(Movie movie){
		movieRepository.save(movie);
		return "redirect:/index";
	}
	
	@RequestMapping(value="/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteBook(@PathVariable("id") Long movieId, Model model) {
		movieRepository.deleteById(movieId);
		return "redirect:/index";
	}
	

}

package com.santtuhyvarinen.moviereviews.web;

import java.io.IOException;

import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.santtuhyvarinen.moviereviews.domain.Movie;
import com.santtuhyvarinen.moviereviews.domain.Review;
import com.santtuhyvarinen.moviereviews.domain.Score;
import com.santtuhyvarinen.moviereviews.domain.User;
import com.santtuhyvarinen.moviereviews.domain.UserDTO;
import com.santtuhyvarinen.moviereviews.interfaces.GenreRepository;
import com.santtuhyvarinen.moviereviews.interfaces.MovieRepository;
import com.santtuhyvarinen.moviereviews.interfaces.ReviewRepository;
import com.santtuhyvarinen.moviereviews.interfaces.UserRepository;
import com.santtuhyvarinen.moviereviews.MovieUtil;
import com.santtuhyvarinen.moviereviews.UserService;

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
	
	@Autowired
	private UserService userService;

	//List of all movies
	@RequestMapping("/index")
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
	
	//Login to the service
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	//Register to the service
	@RequestMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new UserDTO());
		
		return "register";
	}
	
	//Try to create a new user
	@PostMapping("/registerUser") 
	public String registerUser(UserDTO userDTO, RedirectAttributes redirectAttrs){
		
		boolean success = userService.registerUser(userDTO);

		if(!success) {
			redirectAttrs.addFlashAttribute("error", "Username has already been taken by another user - please choose another one");
			return "redirect:/register";
		}
		
		redirectAttrs.addFlashAttribute("registered", true);
		return "redirect:/login";
	}
	
	//Admin page - user management
	@RequestMapping("/adminpage")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String adminPage(Model model) {

		List<User> users = (List<User>) userRepository.findAll();

		User admin = null;
		//Remove admin from the user list
		for(User user : users) {
			if(user.getUsername().equals("admin")) {
				admin = user;
			}
		}
		
		if(admin != null) {
			users.remove(admin);
		}
		
		model.addAttribute("users", users);
		return "adminpage";
	}

	//Delete user from database
	@RequestMapping(value="/delete/user/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteUser(@PathVariable("id") Long userId, Model model) {
		userRepository.deleteById(userId);
		return "redirect:/adminpage";
	}
	
	//Movie's own page
	@RequestMapping(value="/movie/{id}")
	public String moviePage(@PathVariable("id") Long movieId, Model model) {

		//Get currently logged in user to set the rating
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		User user = userRepository.findByUsername(username);
		
		Movie movie = movieRepository.findById(movieId).get();
		
		//Set the poster
		byte[] bytes = movie.getPosterData();
		if(bytes != null) {

	        String base64Encoded = Base64.encodeBase64String(bytes);
	        movie.setBase64ImagePoster(base64Encoded);
		}
        
		//Calculate average score and votes for the movie
		List<Review> reviews = (List<Review>) reviewRepository.findAll();
		double averageScore = MovieUtil.calculateAverageScoreFromReviews(movie, reviews);
		int votes = MovieUtil.calculateVotes(movie, reviews);
		movie.setAverageScore(averageScore);
		movie.setVotes(votes);
		
		//Score of the movie that user has rated
		Score score = new Score();
		
		//Check if user has already left a review for the movie. If the list is empty, user hasn't reviewed the movie yet
		List<Review> leftReview = reviewRepository.findByMovieAndUser(movie, user);
		
		if(leftReview.size() > 0) {
			//User has left a review previously - Set the score
			score.setScore((int)leftReview.get(0).getScore());
		}

		System.out.println(username + "");
		model.addAttribute("movie", movie);
		model.addAttribute("score", score);
		
		return "moviepage";
	}
	
	
	//Add a movie
	@RequestMapping(value="/add")
	public String addMovie(Model model) {
		model.addAttribute("title","Add a movie to the collection");
		model.addAttribute("movie", new Movie());
		model.addAttribute("genres", genreRepository.findAll());
		return "editmovie";
	}
	
	//Edit movie information
	@RequestMapping(value="/edit/{id}")
	public String editMovie(@PathVariable("id") Long movieId, Model model) {
		model.addAttribute("title", "Edit the movie");
		model.addAttribute("movie", movieRepository.findById(movieId).get());
		model.addAttribute("genres", genreRepository.findAll());
		return "editmovie";
	}
	
	//Leave a review
	@RequestMapping("/leaverating/{username}/{movieid}") 
	public String leaveRating(@PathVariable("movieid") Long movieId, @PathVariable("username") String userName, Score score) {

		Movie movie = movieRepository.findById(movieId).get();
		User user = userRepository.findByUsername(userName);
		
		//Check if user has already left a review for the movie. If the list is empty, user hasn't reviewed the movie yet
		List<Review> reviews = reviewRepository.findByMovieAndUser(movie, user);
		
		Review review;
		
		if(reviews.size() > 0) {
			//Update previous review
			review = reviews.get(0);
			review.setScore(score.getScore());
		} else {
			//Leave a new review
			review = new Review(movie, user, score.getScore());
		}
		
		reviewRepository.save(review);
		
		return "redirect:/movie/" + movieId;
	}
	
	//Save movie to database
	@PostMapping("/save") 
	public String save(Movie movie){
		movieRepository.save(movie);
		return "redirect:/movie/" + movie.getId();
	}
	
	//Delete movie from database
	@RequestMapping(value="/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteMovie(@PathVariable("id") Long movieId, Model model) {
		movieRepository.deleteById(movieId);
		return "redirect:/index";
	}

	//Upload a poster for the movie
	@PostMapping(value="/uploadfile/{id}")
	public String submit(@PathVariable("id") Long movieId, @RequestParam("file") MultipartFile file) {
		
		if(file != null && !file.isEmpty()) {
			try {
				//Save file as byte[] array
				byte[] bytes = file.getBytes();
				Movie movie = movieRepository.findById(movieId).get();
				movie.setPosterData(bytes);
	            movieRepository.save(movie);
	         
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    return "redirect:/movie/" + movieId;
	}
	
	//REST API
	@RequestMapping("/api/movies") 
	public @ResponseBody List<Movie> getAllMoviesREST(){
		return (List<Movie>) movieRepository.findAll();
	}

	@RequestMapping("/api/movies/{title}") 
	public @ResponseBody List<Movie> getMovieREST(@PathVariable("title") String title){
		return (List<Movie>) movieRepository.findByTitle(title);
	}
}

package com.santtuhyvarinen.moviereviews.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santtuhyvarinen.moviereviews.interfaces.GenreRepository;
import com.santtuhyvarinen.moviereviews.interfaces.MovieRepository;

@Controller
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	private GenreRepository genreRepository;
	
	@RequestMapping
	public String index() {
		return "index";
	}
}

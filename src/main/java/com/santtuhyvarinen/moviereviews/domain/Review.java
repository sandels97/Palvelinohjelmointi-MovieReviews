package com.santtuhyvarinen.moviereviews.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	//From 1 to 5
	private long score;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="movieId")
	private Movie movie;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="userId")
	private User user;
	
	public Review(Movie movie, User user, long score) {
		this.score = score;
		this.movie = movie;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		//Clamp the score between 1 and 5
		score = Math.min(5, Math.max(1,score));
		this.score = score;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

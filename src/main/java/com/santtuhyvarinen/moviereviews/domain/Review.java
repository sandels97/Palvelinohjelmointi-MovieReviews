package com.santtuhyvarinen.moviereviews.domain;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Represents a movie review left by an user

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private long score; //Review score from 1 to 5
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="movieid")
	private Movie movie; //Movie that was reviewed
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="userid")
	private User user; //User who left the review
	
	public Review() {}
	
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

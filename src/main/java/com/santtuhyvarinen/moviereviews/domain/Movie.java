package com.santtuhyvarinen.moviereviews.domain;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Movie {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String title;
	private long releaseYear;
	
	@Lob
	private byte[] posterData;
	
	@JsonIgnore
	@Transient
	private String base64ImagePoster = "";
	
	@JsonIgnore
	@Transient
	private double averageScore = 0; //Average score of the movie. Do not save it to database, because it's calculated from the reviews on runtime. 
	
	@JsonIgnore
	@Transient
	private int votes = 0; //Review count for the movie. Do not save it to database, because it's calculated from the reviews on runtime.
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="genreid")
	private Genre genre;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="movie")
	private List<Review> reviews;

	public Movie() {}
	
	public Movie(String title, long releaseYear, Genre genre) {
		this.title = title;
		this.releaseYear = releaseYear;
		this.genre = genre;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(long releaseYear) {
		this.releaseYear = releaseYear;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	public byte[] getPosterData() {
		return posterData;
	}

	public void setPosterData(byte[] posterData) {
		this.posterData = posterData;
	}

	public String getBase64ImagePoster() {
		return base64ImagePoster;
	}

	public void setBase64ImagePoster(String base64ImagePoster) {
		this.base64ImagePoster = base64ImagePoster;
	}
}

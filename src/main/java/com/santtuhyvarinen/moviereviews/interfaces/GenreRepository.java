package com.santtuhyvarinen.moviereviews.interfaces;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import com.santtuhyvarinen.moviereviews.domain.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long>{
	List<Genre> findByName(String name);
}

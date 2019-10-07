package com.santtuhyvarinen.moviereviews.interfaces;

import org.springframework.data.repository.CrudRepository;

import com.santtuhyvarinen.moviereviews.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByUsername(String username);
}


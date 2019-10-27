package com.santtuhyvarinen.moviereviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.santtuhyvarinen.moviereviews.domain.User;
import com.santtuhyvarinen.moviereviews.domain.UserDTO;
import com.santtuhyvarinen.moviereviews.interfaces.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//Register new user - return true if user registered successfully, return false if registration failed (e.g. username was already taken)
	public boolean registerUser(UserDTO userDTO) {

		User existingUser = userRepository.findByUsername(userDTO.getUsername());
		if(existingUser != null) {
			//Account already exists
			return false;
		} 
		
		String username = userDTO.getUsername();
		String password = userDTO.getPassword();
		
		//Encode the password
		String passwordHash = passwordEncoder.encode(password);
		
		User user = new User(username, passwordHash, "USER");

		userRepository.save(user);
		
		return true;
	}
}

package com.cooksys.socialmediaassignment.team2.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmediaassignment.team2.entities.Hashtag;
import com.cooksys.socialmediaassignment.team2.entities.User;
import com.cooksys.socialmediaassignment.team2.exceptions.NotFoundException;
import com.cooksys.socialmediaassignment.team2.repositories.HashtagRepository;
import com.cooksys.socialmediaassignment.team2.repositories.UserRepository;
import com.cooksys.socialmediaassignment.team2.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	
	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;
	
	// - - - HELPER METHOD - - - \\
	public Hashtag getHashtag(String label) {
  		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
  		if(optionalHashtag.isEmpty()) 
  			{ throw new NotFoundException("No hashtag found with the name: " +label); }
  		return optionalHashtag.get();
  	}
	
	
	@Override // checks whether or not a given hashtag exists
	public boolean doesHashtagExist(String label) { 
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
  		if(optionalHashtag.isEmpty()) { return false; }
		return true;
	}

	@Override // checks whether or not a given username exists
	public boolean doesUsernameExist(String username) { 
		Optional<User> optionalUsername = userRepository.findByCredentialsUsername(username);
		return !optionalUsername.isEmpty(); // return false if empty
	}

	@Override // checks whether or not a given username is available
	public boolean isUsernameAvailable(String username) { 
		Optional<User> optionalUsername = userRepository.findByCredentialsUsername(username);
		return optionalUsername.isEmpty(); // return true if empty, returns false if username exists at all
	}
}

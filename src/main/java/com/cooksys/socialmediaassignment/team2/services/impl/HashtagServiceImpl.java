package com.cooksys.socialmediaassignment.team2.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmediaassignment.team2.dtos.HashtagDto;
import com.cooksys.socialmediaassignment.team2.dtos.TweetResponseDto;
import com.cooksys.socialmediaassignment.team2.entities.Hashtag;
import com.cooksys.socialmediaassignment.team2.exceptions.NotFoundException;
import com.cooksys.socialmediaassignment.team2.mappers.HashtagMapper;
import com.cooksys.socialmediaassignment.team2.mappers.TweetMapper;
import com.cooksys.socialmediaassignment.team2.repositories.HashtagRepository;
import com.cooksys.socialmediaassignment.team2.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
	
	private final HashtagRepository hashtagRepository;
	private final HashtagMapper hashtagMapper;
	
	private final TweetMapper tweetMapper;
	
	// - - - HELPER METHOD - - - \\
	public Hashtag getHashtag(String label) {
  		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
  		if(optionalHashtag.isEmpty()) 
  			{ throw new NotFoundException("No hashtag found with the name: " +label); }
  		return optionalHashtag.get();
  	}
	
	

	@Override // Retrieves all hashtags tracked by the database
	public List<HashtagDto> getAllHashtags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	@Override // Retrieves all tweets tagged with the given hashtag label
	public List<TweetResponseDto> getOneHashtag(String label) {
		return tweetMapper.entitiesToResponseDtos(getHashtag(label).getTweetList());
	}
}
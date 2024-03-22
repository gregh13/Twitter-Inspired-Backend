package com.cooksys.socialmediaassignment.team2.services;

import java.util.List;

import com.cooksys.socialmediaassignment.team2.dtos.HashtagDto;
import com.cooksys.socialmediaassignment.team2.dtos.TweetResponseDto;
import com.cooksys.socialmediaassignment.team2.entities.Hashtag;
import com.cooksys.socialmediaassignment.team2.entities.Tweet;

public interface HashtagService {

	List<HashtagDto> getAllHashtags();

	List<TweetResponseDto> getOneHashtag(String label);


}

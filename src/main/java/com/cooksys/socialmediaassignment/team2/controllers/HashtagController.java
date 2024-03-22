package com.cooksys.socialmediaassignment.team2.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmediaassignment.team2.dtos.HashtagDto;
import com.cooksys.socialmediaassignment.team2.dtos.TweetResponseDto;
import com.cooksys.socialmediaassignment.team2.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
	
	private final HashtagService hashtagService;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<HashtagDto> getAllHashtags() {
		return hashtagService.getAllHashtags();
		// Retrieves all hashtags tracked by the database.
	}
	
	@GetMapping("/{label}")
	@ResponseStatus(HttpStatus.OK)
	public List<TweetResponseDto> getOneHashtag(@PathVariable("label") String label) {
		return hashtagService.getOneHashtag(label);
		// Retrieves all tweets tagged with the given hashtag label.
	}

}

package com.cooksys.socialmediaassignment.team2.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmediaassignment.team2.dtos.CredentialsDto;
import com.cooksys.socialmediaassignment.team2.dtos.TweetResponseDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserRequestDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserResponseDto;
import com.cooksys.socialmediaassignment.team2.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }
	
    @GetMapping("/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public  UserResponseDto getUserByUsername(@PathVariable("username") String username) {
    	return userService.getUserByUsername(username);
    }
    
    @GetMapping("/@{username}/tweets")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseDto> getTweetsByUsername(@PathVariable("username") String username) {
    	return userService.getTweetsByUsername(username);
    }
    
    @GetMapping("/@{username}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseDto> getTweetFeedByUsername(@PathVariable("username") String username) {
    	return userService.getTweetFeedByUsername(username);
    }
    
    @GetMapping("/@{username}/followers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getUserFollowers(@PathVariable("username") String username) {
    	return userService.getUserFollowers(username);
    }
    
    @GetMapping("/@{username}/following")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getUserFollowingList(@PathVariable("username") String username) {
    	return userService.getUserFollowingList(username);
    }
    
    @GetMapping("/@{username}/mentions")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseDto> getUserMentions(@PathVariable("username") String username) {
    	return userService.getUserMentions(username);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }
    
    @PostMapping("/@{username}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public void followUser(@PathVariable("username") String username, @RequestBody CredentialsDto credentialsDto) {
    	userService.followUser(username, credentialsDto);
    }
    
    @PostMapping("/@{username}/unfollow")
    @ResponseStatus(HttpStatus.CREATED)
    public void unfollowUser(@PathVariable("username") String username, @RequestBody CredentialsDto credentialsDto) {
    	userService.unfollowUser(username, credentialsDto);
    }
    
    @PatchMapping("/@{username}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserResponseDto updateProfile(@PathVariable("username") String username, @RequestBody UserRequestDto userRequestDto) {
    	return userService.updateProfile(username, userRequestDto);
    }
    
    @DeleteMapping("/@{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto deleteUser(@PathVariable("username") String username, @RequestBody CredentialsDto credentialsDto) {
    	return userService.deleteUser(username, credentialsDto);
    }
    
}

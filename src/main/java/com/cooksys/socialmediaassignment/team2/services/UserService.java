package com.cooksys.socialmediaassignment.team2.services;

import java.util.List;

import com.cooksys.socialmediaassignment.team2.dtos.CredentialsDto;
import com.cooksys.socialmediaassignment.team2.dtos.TweetResponseDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserRequestDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserResponseDto;

public interface UserService {

	  List<UserResponseDto> getAllUsers();

	  UserResponseDto getUserByUsername(String username);

	  List<TweetResponseDto> getTweetsByUsername(String username);

	  List<TweetResponseDto> getTweetFeedByUsername(String username);

	  List<UserResponseDto> getUserFollowers(String username);

	  List<UserResponseDto> getUserFollowingList(String username);

	  List<TweetResponseDto> getUserMentions(String username);

	  UserResponseDto createUser(UserRequestDto userRequestDto);

	  void followUser(String username, CredentialsDto credentialsDto);

	  void unfollowUser(String username, CredentialsDto credentialsDto);

	  UserResponseDto updateProfile(String username, UserRequestDto userRequestDto);

	  UserResponseDto deleteUser(String username, CredentialsDto credentialsDto);




}

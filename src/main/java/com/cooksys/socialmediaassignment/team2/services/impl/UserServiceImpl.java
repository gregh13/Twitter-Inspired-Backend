package com.cooksys.socialmediaassignment.team2.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cooksys.socialmediaassignment.team2.dtos.CredentialsDto;
import com.cooksys.socialmediaassignment.team2.dtos.ProfileDto;
import com.cooksys.socialmediaassignment.team2.dtos.TweetResponseDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserRequestDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserResponseDto;
import com.cooksys.socialmediaassignment.team2.entities.Profile;
import com.cooksys.socialmediaassignment.team2.entities.Tweet;
import com.cooksys.socialmediaassignment.team2.entities.User;
import com.cooksys.socialmediaassignment.team2.exceptions.BadRequestException;
import com.cooksys.socialmediaassignment.team2.exceptions.NotFoundException;
import com.cooksys.socialmediaassignment.team2.mappers.CredentialsMapper;
import com.cooksys.socialmediaassignment.team2.mappers.ProfileMapper;
import com.cooksys.socialmediaassignment.team2.mappers.TweetMapper;
import com.cooksys.socialmediaassignment.team2.mappers.UserMapper;
import com.cooksys.socialmediaassignment.team2.repositories.TweetRepository;
import com.cooksys.socialmediaassignment.team2.repositories.UserRepository;
import com.cooksys.socialmediaassignment.team2.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;

	private final CredentialsMapper credentialsMapper;

	private final ProfileMapper profileMapper;

	private User authenticateUser(CredentialsDto credentialsDto) {
		// validate credentials
		validateCredentials(credentialsDto);

		Optional<User> userOptional = userRepository
				.findByCredentialsUsernameAndDeletedFalse(credentialsDto.getUsername());
		if (userOptional.isPresent()) {
			if (credentialsDto.getPassword().equals(userOptional.get().getCredentials().getPassword())) {
				return userOptional.get();
			}
		}
		throw new BadRequestException("Invalid credentials.");
	}

	private void validateCredentials(CredentialsDto credentialsDto) {
		if (credentialsDto == null) {
			throw new BadRequestException("Bad Request: Credentials are missing from request");
		}
		if (credentialsDto.getUsername() == null) {
			throw new BadRequestException("Bad Request: Credentials must include username");
		}
		if (credentialsDto.getPassword() == null) {
			throw new BadRequestException("Bad Request: Credentials must include password");
		}
	}

	private void validateProfile(ProfileDto profileDto){
		if (profileDto == null) {
			throw new BadRequestException("Bad Request: Profile must include email");
		}

		if (profileDto.getEmail() == null) {
			throw new BadRequestException("Bad Request: Profile must include email");
		}
	}

	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {

		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		// checks to see if user exists
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// returns user
			return userMapper.entityToDto(user);
		} else {
			throw new NotFoundException("Not Found: That user does not exist.");
		}
	}

	@Override
	public List<TweetResponseDto> getTweetsByUsername(String username) {

		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		// checks to see if user exists
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// grabs non-deleted tweets from user
			List<Tweet> tweets = tweetRepository.findAllByAuthorAndDeletedFalseOrderByPostedDesc(user);
			// returns list of tweets
			return tweetMapper.entitiesToResponseDtos(tweets);
		} else {
			throw new NotFoundException("User with username " + username + " not found or deleted.");
		}
	}

	@Override
	public List<TweetResponseDto> getTweetFeedByUsername(String username) {

		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		// check to see if user exists
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// creates empty feed list
			List<Tweet> userFeed = new ArrayList<>();
			// grabs non-deleted tweets from user and adds to userFeed
			List<Tweet> userTweets = tweetRepository.findAllByAuthorAndDeletedFalseOrderByPostedDesc(user);
			userFeed.addAll(userTweets);

			// grabs all non-deleted tweets from all users in targeted users following list
			List<User> followingList = user.getFollowingList();
			for (User followingUser : followingList) {
				List<Tweet> followerTweets = tweetRepository
						.findAllByAuthorAndDeletedFalseOrderByPostedDesc(followingUser);
				userFeed.addAll(followerTweets);
			}
			// sorts list by reverse-chronological order based on the posted timestamp
			userFeed.sort(Comparator.comparing(Tweet::getPosted).reversed());
			// return user feed
			return tweetMapper.entitiesToResponseDtos(userFeed);
		} else {
			throw new NotFoundException("User with username " + username + " not found or deleted.");
		}
	}

	@Override
	public List<UserResponseDto> getUserFollowers(String username) {

		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		// check to see if user exists
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// creates empty array for activeUsers
			List<User> activeUsers = new ArrayList<>();
			// gets follower list
			List<User> followerList = user.getFollowerList();
			// checks to see if the follower is active and then adds it to a list
			for (User followerUser : followerList) {
				Optional<User> follower = userRepository
						.findByCredentialsUsernameAndDeletedFalse(followerUser.getCredentials().getUsername());
				if (follower.isPresent()) {
					activeUsers.add(follower.get());
				}
			}
			// return active followers
			return userMapper.entitiesToDtos(activeUsers);
		} else {
			throw new NotFoundException("User with username " + username + " not found or deleted.");
		}
	}

	@Override
	public List<UserResponseDto> getUserFollowingList(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		// check to see if user exists
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// creates empty array for activeUsers
			List<User> activeFollowingUsers = new ArrayList<>();
			// gets following list
			List<User> followingList = user.getFollowingList();
			// checks to see if the following user is active and then adds it to a list
			for (User followingUser : followingList) {
				Optional<User> following = userRepository
						.findByCredentialsUsernameAndDeletedFalse(followingUser.getCredentials().getUsername());
				if (following.isPresent()) {
					activeFollowingUsers.add(following.get());
				}
			}
			// return active following user list
			return userMapper.entitiesToDtos(activeFollowingUsers);
		} else {
			throw new NotFoundException("User with username " + username + " not found or deleted.");
		}
	}

	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);

		// check to see if user exists
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			// retrieve user mention list
			List<Tweet> mentionedTweets = user.getMentionList();
			// filter out deleted tweets
			mentionedTweets = mentionedTweets.stream().filter(tweet -> !tweet.isDeleted()).collect(Collectors.toList());

			// sort tweets in reverse-chronological order
			mentionedTweets.sort(Comparator.comparing(Tweet::getPosted).reversed());

			List<TweetResponseDto> tweetResponseDtos = tweetMapper.entitiesToResponseDtos(mentionedTweets);

			return tweetResponseDtos;
		} else {
			throw new NotFoundException("User with username " + username + " not found or deleted.");
		}
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {

		if (userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Missing required fields in the request.");
		}

		CredentialsDto credentialsDto = userRequestDto.getCredentials();
		ProfileDto profileDto = userRequestDto.getProfile();

		// Validate request Credentials and Profile
		validateCredentials(credentialsDto);
		validateProfile(profileDto);

        //check if the username is already taken
        Optional<User> existingUser = userRepository.findByCredentialsUsername(credentialsDto.getUsername());
        if (existingUser.isPresent()) {

            if (!existingUser.get().isDeleted()) {
                throw new BadRequestException("Username is already taken.");
            } else {
                //reactivate the deleted user
                User user = existingUser.get();
                user.setDeleted(false);
                userRepository.save(user);
                return userMapper.entityToDto(user);
            }
        }

		// create a new user
		User newUser = new User();
		newUser.setCredentials(credentialsMapper.requestDtoToEntity(credentialsDto));
		newUser.setProfile(profileMapper.requestDtoToEntity(profileDto));
		newUser.setDeleted(false);
		userRepository.save(newUser);
		return userMapper.entityToDto(newUser);
	}

	@Override
	public void followUser(String username, CredentialsDto credentialsDto) {
	    Optional<User> userToFollowOptional = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
	    if (userToFollowOptional.isPresent()) {
	        User userToFollow = userToFollowOptional.get();
	        
	        // authenticate the user
	        User currentUser = authenticateUser(credentialsDto);

	        // check if the user is already following the user to follow
			if (!userToFollow.getFollowerList().contains(currentUser)) {
				// add the user to follow to the current user's following list
				userToFollow.getFollowerList().add(currentUser);
				userRepository.save(userToFollow);
	        } else {
	            throw new BadRequestException("You are already following this user.");
	        }
	    } else {
	        throw new NotFoundException("No such user exists.");
	    }
	}

	@Override
	public void unfollowUser(String username, CredentialsDto credentialsDto) {
		Optional<User> userToUnfollowOptional = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (userToUnfollowOptional.isPresent()) {
			User userToUnfollow = userToUnfollowOptional.get();

			// authenticate the user
			User authenticatedUser = authenticateUser(credentialsDto);

			// check if there is an existing follower relationship
			if (userToUnfollow.getFollowerList().contains(authenticatedUser)) {

				// remove the authenticated user from the user to unfollow follower list
				userToUnfollow.getFollowerList().remove(authenticatedUser);

				userRepository.save(userToUnfollow);
			} else {
				throw new NotFoundException("No following relationship found.");
			}
		} else {
			throw new NotFoundException("User to unfollow not found.");
		}
	}

	@Override
	public UserResponseDto updateProfile(String username, UserRequestDto userRequestDto) {
		// authenticate the user
		User authenticatedUser = authenticateUser(userRequestDto.getCredentials());

		// find the user with the given username
		Optional<User> userOptional = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			// check if the authenticated user matches the user to be updated
			if (!user.equals(authenticatedUser)) {
				throw new BadRequestException("You are not authorized to update this user's profile.");
			}

			// update the user's profile
			ProfileDto newProfile = userRequestDto.getProfile();
			Profile userProfile = user.getProfile();

			if (newProfile == null) {
				throw new BadRequestException("Bad Request: Must include Profile in request body");
			}

			if (newProfile.getFirstName() != null) {
				userProfile.setFirstName(newProfile.getFirstName());
			}
			if (newProfile.getLastName() != null) {
				userProfile.setLastName(newProfile.getLastName());
			}
			if (newProfile.getEmail() != null) {
				userProfile.setEmail(newProfile.getEmail());
			}
			if (newProfile.getPhone() != null) {
				userProfile.setPhone(newProfile.getPhone());
			}

			// save changes to the database
			userRepository.save(user);

			// return the updated user data
			return userMapper.entityToDto(user);
		} else {
			throw new NotFoundException("User not found.");
		}
	}

	@Override
	public UserResponseDto deleteUser(String username, CredentialsDto credentialsDto) {
		// authenticate the user
		User authenticatedUser = authenticateUser(credentialsDto);

		// find the user with the given username
		Optional<User> userOptional = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();

			// set user to be deleted
			user.setDeleted(true);
			userRepository.save(user);

			return userMapper.entityToDto(user);
		} else {
			throw new NotFoundException("User is not found.");
		}
	}

}

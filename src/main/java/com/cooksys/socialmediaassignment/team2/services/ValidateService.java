package com.cooksys.socialmediaassignment.team2.services;

public interface ValidateService {

	boolean doesHashtagExist(String label);

	boolean doesUsernameExist(String username);

	boolean isUsernameAvailable(String username);


}

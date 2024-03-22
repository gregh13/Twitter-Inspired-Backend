package com.cooksys.socialmediaassignment.team2.dtos;

import java.sql.Timestamp;

import com.cooksys.socialmediaassignment.team2.entities.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {
	
	private String username;
	
	private ProfileDto profile;
	
	private Timestamp joined;
	
}

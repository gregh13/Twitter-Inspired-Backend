package com.cooksys.socialmediaassignment.team2.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class UserRequestDto {
	
	private CredentialsDto credentials;
	
	private ProfileDto profile;
	
}

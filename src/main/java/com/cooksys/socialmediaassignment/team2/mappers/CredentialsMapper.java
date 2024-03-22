package com.cooksys.socialmediaassignment.team2.mappers;

import org.mapstruct.Mapper;

import com.cooksys.socialmediaassignment.team2.dtos.CredentialsDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserRequestDto;
import com.cooksys.socialmediaassignment.team2.entities.Credentials;
import com.cooksys.socialmediaassignment.team2.entities.User;


@Mapper(componentModel = "spring")
public interface CredentialsMapper {

	CredentialsDto entityToDto(Credentials credentials);
	
	Credentials requestDtoToEntity(CredentialsDto credentialsRequestDto);
	
}

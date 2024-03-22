package com.cooksys.socialmediaassignment.team2.mappers;

import org.mapstruct.Mapper;

import com.cooksys.socialmediaassignment.team2.dtos.ProfileDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserRequestDto;
import com.cooksys.socialmediaassignment.team2.entities.Profile;
import com.cooksys.socialmediaassignment.team2.entities.User;


@Mapper(componentModel = "spring")
public interface ProfileMapper {

	ProfileDto entityToDto(Profile profile);
	
	Profile requestDtoToEntity(ProfileDto profileRequestDto);
}

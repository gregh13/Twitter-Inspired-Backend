package com.cooksys.socialmediaassignment.team2.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.socialmediaassignment.team2.dtos.UserRequestDto;
import com.cooksys.socialmediaassignment.team2.dtos.UserResponseDto;
import com.cooksys.socialmediaassignment.team2.entities.User;

@Mapper(componentModel = "spring", uses = { CredentialsMapper.class, ProfileMapper.class })
public interface UserMapper {

	@Mapping(target = "username", source = "credentials.username")
    UserResponseDto entityToDto(User entity);

    List<UserResponseDto> entitiesToDtos(List<User> entities);

    User requestDtoToEntity(UserRequestDto userRequestDto);
    
}

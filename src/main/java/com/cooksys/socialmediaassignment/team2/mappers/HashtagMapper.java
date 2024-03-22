package com.cooksys.socialmediaassignment.team2.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialmediaassignment.team2.dtos.HashtagDto;
import com.cooksys.socialmediaassignment.team2.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
	
	Hashtag hashtagDtoToEntity(HashtagDto hashtagDto);
	
	List<HashtagDto> entitiesToDtos(List<Hashtag> hashtag);


}

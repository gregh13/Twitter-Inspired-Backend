package com.cooksys.socialmediaassignment.team2.mappers;

import com.cooksys.socialmediaassignment.team2.dtos.TweetRequestDto;
import com.cooksys.socialmediaassignment.team2.dtos.TweetResponseDto;
import com.cooksys.socialmediaassignment.team2.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TweetMapper {
    TweetResponseDto entityToResponseDto(Tweet entity);

    List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> entities);

    Tweet requestDtoToEntity(TweetRequestDto requestDto);

}

package com.cooksys.socialmediaassignment.team2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContextDto {
    private TweetResponseDto target;

    private List<TweetResponseDto> before;

    private List<TweetResponseDto> after;

}
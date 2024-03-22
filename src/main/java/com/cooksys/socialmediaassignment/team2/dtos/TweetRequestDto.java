package com.cooksys.socialmediaassignment.team2.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class TweetRequestDto {

    private CredentialsDto credentials;

    private String content;
}
package com.cooksys.socialmediaassignment.team2.services;

import com.cooksys.socialmediaassignment.team2.dtos.*;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getAllTweets();

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    TweetResponseDto getOneTweet(Long id);

    TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto);

    void likeTweet(Long id, CredentialsDto credentialsDto);

    TweetResponseDto replyTweet(Long id, TweetRequestDto tweetRequestDto);

    TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto);

    List<HashtagDto> getAllTags(Long id);

    List<UserResponseDto> getAllLikes(Long id);

    ContextDto getContext(Long id);

    List<TweetResponseDto> getReplies(Long id);

    List<TweetResponseDto> getReposts(Long id);

    List<UserResponseDto> getMentions(Long id);
}

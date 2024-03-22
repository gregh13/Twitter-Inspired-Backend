package com.cooksys.socialmediaassignment.team2.controllers;

import com.cooksys.socialmediaassignment.team2.dtos.*;
import com.cooksys.socialmediaassignment.team2.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getAllTweets();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.createTweet(tweetRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TweetResponseDto getOneTweet(@PathVariable("id") Long id) {
        return tweetService.getOneTweet(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TweetResponseDto deleteTweet(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.deleteTweet(id, credentialsDto);
    }

    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public void likeTweet(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto) {
        tweetService.likeTweet(id, credentialsDto);
    }

    @PostMapping("/{id}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto replyTweet(@PathVariable("id") Long id, @RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.replyTweet(id, tweetRequestDto);
    }

    @PostMapping("/{id}/repost")
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto repostTweet(@PathVariable("id") Long id, @RequestBody CredentialsDto credentialsDto) {
        return tweetService.repostTweet(id, credentialsDto);
    }

    @GetMapping("/{id}/tags")
    @ResponseStatus(HttpStatus.OK)
    public List<HashtagDto> getAllTags(@PathVariable("id") Long id) {
        return tweetService.getAllTags(id);
    }

    @GetMapping("/{id}/likes")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllLikes(@PathVariable("id") Long id) {
        return tweetService.getAllLikes(id);
    }

    @GetMapping("/{id}/context")
    @ResponseStatus(HttpStatus.OK)
    public ContextDto getContext(@PathVariable("id") Long id) {
        return tweetService.getContext(id);
    }

    @GetMapping("/{id}/replies")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseDto> getReplies(@PathVariable("id") Long id) {
        return tweetService.getReplies(id);
    }

    @GetMapping("/{id}/reposts")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetResponseDto> getReposts(@PathVariable("id") Long id) {
        return tweetService.getReposts(id);
    }

    @GetMapping("/{id}/mentions")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getMentions(@PathVariable("id") Long id) {
        return tweetService.getMentions(id);
    }
}

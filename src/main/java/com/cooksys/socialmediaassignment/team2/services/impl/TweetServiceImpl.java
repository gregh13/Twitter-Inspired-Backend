package com.cooksys.socialmediaassignment.team2.services.impl;

import com.cooksys.socialmediaassignment.team2.dtos.*;
import com.cooksys.socialmediaassignment.team2.entities.Hashtag;
import com.cooksys.socialmediaassignment.team2.entities.Tweet;
import com.cooksys.socialmediaassignment.team2.entities.User;
import com.cooksys.socialmediaassignment.team2.exceptions.BadRequestException;
import com.cooksys.socialmediaassignment.team2.exceptions.NotFoundException;
import com.cooksys.socialmediaassignment.team2.mappers.HashtagMapper;
import com.cooksys.socialmediaassignment.team2.mappers.TweetMapper;
import com.cooksys.socialmediaassignment.team2.mappers.UserMapper;
import com.cooksys.socialmediaassignment.team2.repositories.HashtagRepository;
import com.cooksys.socialmediaassignment.team2.repositories.TweetRepository;
import com.cooksys.socialmediaassignment.team2.repositories.UserRepository;
import com.cooksys.socialmediaassignment.team2.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private static final Pattern hashtagPattern = Pattern.compile("#(\\w+)");
    private static final Pattern mentionPattern = Pattern.compile("@(\\w+)");

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Bad Request: Tweet id must be an integer greater than 0");
        }
    }

    private void validateContent(String content) {
        if (content == null) {
            throw new BadRequestException("Bad Request: Tweet content cannot be empty");
        }
    }

    private void validateTweet(Tweet tweet, Long id) {
        if (tweet == null) {
            throw new NotFoundException("Not Found: Could not find tweet with id: " + id);
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new NotFoundException("Not Found: Could not find user associated with these credentials");
        }
    }

    private void validateTweetAndUserLink(Tweet tweet, User user, Long tweetId) {
        // Validate tweet and user
        validateTweet(tweet, tweetId);
        validateUser(user);

        // Ensure user matches tweet author
        if (tweet.getAuthor() != user) {
            throw new BadRequestException("Bad Request: user credentials and tweet author don't match");
        }
    }

    private static List<String> extractContent(String content, Pattern pattern) {
        // Initialize matcher and matches list
        List<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(content);

        // Iterate through matcher to get all matches of pattern
        while (matcher.find()) {
            // Uses the first (and only) group of the pattern and adds it to matches
            matches.add(matcher.group(1));
        }

        return matches;
    }

    private Tweet processTweetHashtags(Tweet tweet) {
        // Get all hashtag labels in tweet content using regex pattern matcher
        List<String> hashtagList = extractContent(tweet.getContent(), hashtagPattern);

        // Create empty list if not initialized
        if (tweet.getTagList() == null) {
            tweet.setTagList(new ArrayList<>());
        }

        // Initialize hashtag for loop below
        Hashtag hashtag;
        for (String label : hashtagList) {
            // Read existing label from DB if it exists
            Optional<Hashtag> hashtagOptional = hashtagRepository.findByLabel(label);

            if (hashtagOptional.isEmpty()) {
                // Label doesn't exist in DB, need to create new one via dto --> entity
                HashtagDto hashtagDto = new HashtagDto();
                hashtagDto.setLabel(label);

                // Write new hashtag to DB
                hashtag = hashtagRepository.saveAndFlush(hashtagMapper.hashtagDtoToEntity(hashtagDto));

            } else {
                // Label exists in DB already
                hashtag = hashtagOptional.get();
            }

            // Update hashtag and tweet lists through join table owner --> tweet taglist
            tweet.getTagList().add(hashtag);
        }

        // Update tweet in DB and return tweet
        return tweetRepository.saveAndFlush(tweet);
    }

    private Tweet processTweetMentions(Tweet tweet) {
        // Get all mentioned users in tweet content using regex pattern matcher
        List<String> mentionList = extractContent(tweet.getContent(), mentionPattern);

        // Create empty list if not initialized
        if (tweet.getMentionList() == null) {
            tweet.setMentionList(new ArrayList<>());
        }

        // Create user object for loop below
        User user;

        for (String username : mentionList) {
            // Read existing User from DB if it exists
            Optional<User> userOptional = userRepository.findByCredentialsUsername(username);

            if (userOptional.isEmpty()) {
                // Can't process mention for a user not in DB
                continue;
            }

            // User in DB (even inactive users will still have their mentions updated)
            user = userOptional.get();

            // Update user and tweet lists through join table owner --> tweet mentionlist
            tweet.getMentionList().add(user);
        }

        // Update tweet in DB and return tweet
        return tweetRepository.saveAndFlush(tweet);
    }

    private Tweet processTweetContent(Tweet tweet) {
        // Process tweet content
        tweet = processTweetHashtags(tweet);
        tweet = processTweetMentions(tweet);
        return tweet;

    }

    private User getUserByCredentials(CredentialsDto credentialsDto) {
        // Validate credentialsDto fields
        if (credentialsDto == null) {
            throw new BadRequestException("Bad Request: Credentials must be included in the request body");
        }
        if (credentialsDto.getUsername() == null) {
            throw new BadRequestException("Bad Request: Username must be included in the request body");
        }
        if (credentialsDto.getPassword() == null) {
            throw new BadRequestException("Bad Request: Password must be included in the request body");
        }

        // Read user from DB
        Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(credentialsDto.getUsername());

        if (user.isEmpty()) {
            // User not in DB or not active (deleted)
            throw new NotFoundException("Not Found: Could not find an active user with the username: " + credentialsDto.getUsername());
        } else if (!user.get().getCredentials().getPassword().equals(credentialsDto.getPassword())) {
            // Password from request body does not match password for user in DB
            throw new BadRequestException("Bad Request: Password is incorrect");
        }

        // Return validated user
        return user.get();
    }

    @Override
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToResponseDtos(tweetRepository.findAllByDeletedFalseOrderByPostedDesc());
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        // Validate content and credentials
        validateContent(tweetRequestDto.getContent());

        // Validate Credentials and return user
        User user = getUserByCredentials(tweetRequestDto.getCredentials());

        // Create tweet entity
        Tweet tweet = tweetMapper.requestDtoToEntity(tweetRequestDto);

        // Update tweet author
        tweet.setAuthor(user);

        // Process tweet to extract and create mentions & hashtags, and then update tweet list fields
        tweet = processTweetContent(tweet);

        // Write tweet to DB and return responseDto
        return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public TweetResponseDto getOneTweet(Long id) {
        // Validate input
        validateId(id);

        // Read tweet from DB
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate retrieved tweet
        validateTweet(tweet, id);

        return tweetMapper.entityToResponseDto(tweet);
    }

    @Override
    public TweetResponseDto deleteTweet(Long id, CredentialsDto credentialsDto) {
        // Validate input
        validateId(id);

        // Read tweet and user from DB
        User user = getUserByCredentials(credentialsDto);
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet & user both exist and match
        validateTweetAndUserLink(tweet, user, id);

        // 'Delete' tweet
        tweet.setDeleted(true);

        // Update DB and return deleted tweet responseDto
        return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public void likeTweet(Long id, CredentialsDto credentialsDto) {
        // Validate input
        validateId(id);

        // Read tweet and user from DB
        User user = getUserByCredentials(credentialsDto);
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet & user both exist and match
        validateTweet(tweet, id);
        validateUser(user);

        // Update user tweetLikeList and update DB only if user hasn't already liked the tweet
        if (!user.getTweetLikeList().contains(tweet)) {
            user.getTweetLikeList().add(tweet);
            userRepository.saveAndFlush(user);
        }
    }

    @Override
    public TweetResponseDto replyTweet(Long id, TweetRequestDto tweetRequestDto) {
        // Validate input
        validateId(id);
        validateContent(tweetRequestDto.getContent());

        // Read tweet and user from DB
        User user = getUserByCredentials(tweetRequestDto.getCredentials());
        Tweet originalTweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet and user exist
        validateTweet(originalTweet, id);
        validateUser(user);

        // Create reply tweet, update author and inReplyTo fields
        Tweet replyTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
        replyTweet.setAuthor(user);
        replyTweet.setInReplyTo(originalTweet);

        // Process mentions and hashtags in content, update tweet fields/lists
        replyTweet = processTweetContent(replyTweet);

        // Update originalTweet in DB
        tweetRepository.saveAndFlush(originalTweet);

        // Create replyTweet in DB and return responseDto
        return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(replyTweet));
    }

    @Override
    public TweetResponseDto repostTweet(Long id, CredentialsDto credentialsDto) {
        // Validate input
        validateId(id);

        // Read tweet and user from DB
        User user = getUserByCredentials(credentialsDto);
        Tweet originalTweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet and user exist
        validateTweet(originalTweet, id);
        validateUser(user);

        // Create repost tweet, update author, repostOf, and content fields
        Tweet repostTweet = new Tweet();
        repostTweet.setAuthor(user);
        repostTweet.setRepostOf(originalTweet);
        repostTweet.setContent(originalTweet.getContent());

        // Update tag and mention list of repost tweet
        repostTweet.setTagList(new ArrayList<>(originalTweet.getTagList()));
        repostTweet.setMentionList(new ArrayList<>(originalTweet.getMentionList()));

        // Update originalTweet in DB
        tweetRepository.saveAndFlush(originalTweet);

        // Create repostTweet in DB and return responseDto
        return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(repostTweet));
    }

    @Override
    public List<HashtagDto> getAllTags(Long id) {
        // Validate input
        validateId(id);

        // Read tweet from DB
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet exists
        validateTweet(tweet, id);

        return hashtagMapper.entitiesToDtos(tweet.getTagList());
    }

    @Override
    public List<UserResponseDto> getAllLikes(Long id) {
        // Validate input
        validateId(id);

        // Read tweet from DB
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet exists
        validateTweet(tweet, id);

        // Filter out inactive (deleted) users
        List<User> activeLikeList = tweet.getLikeList().stream()
                .filter(user -> !user.isDeleted()).toList();

        return userMapper.entitiesToDtos(activeLikeList);
    }

    @Override
    public ContextDto getContext(Long id) {
        // Validate input
        validateId(id);

        // Read tweet from DB
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet exists
        validateTweet(tweet, id);

        // Initialize variables to get chain of replies before
        List<Tweet> before = new ArrayList<>();
        Tweet tweetBefore = tweet.getInReplyTo();

        // Create the 'before' chain by iteratively adding each link in the reply chain
        while (tweetBefore != null) {
            // Only add non-deleted tweets to chain
            if (!tweetBefore.isDeleted()) {
                before.add(tweetBefore);
            }
            // Prepare for next iteration
            tweetBefore = tweetBefore.getInReplyTo();
        }

        // Reverse list to make it in chronological order
        Collections.reverse(before);

        // Create the 'after' chain --> trickier since we need to get the replies of replies
        List<Tweet> after = new ArrayList<>();
        List<Tweet> remainingReplies = new ArrayList<>(tweet.getReplyList());
        Tweet currentTweet;

        // Use a queue like structure that processes a tweet in the array and adds any direct replies to the list
        while (!remainingReplies.isEmpty()) {
            // Pop last item in list
            currentTweet = remainingReplies.remove(remainingReplies.size() - 1);
            // Add all direct replies to remaining replies.
            // Note: We don't want to filter any deleted replies here since the chain needs to be transitive
            remainingReplies.addAll(currentTweet.getReplyList());
            // Filter out deleted tweets here when adding to the 'after' array
            if (!currentTweet.isDeleted()) {
                after.add(currentTweet);
            }
        }

        // Sort list by timestamp to get chronological order
        after.sort(Comparator.comparing(Tweet::getPosted));

        // Create and return ContextDto
        return new ContextDto(
                tweetMapper.entityToResponseDto(tweet),
                tweetMapper.entitiesToResponseDtos(before),
                tweetMapper.entitiesToResponseDtos(after)
        );
    }

    @Override
    public List<TweetResponseDto> getReplies(Long id) {
        // Validate input
        validateId(id);

        // Read tweet from DB
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet exists
        validateTweet(tweet, id);

        // Filter out inactive (deleted) tweet replies
        List<Tweet> activeDirectReplies = tweet.getReplyList().stream()
                .filter(reply -> !reply.isDeleted()).toList();

        return tweetMapper.entitiesToResponseDtos(activeDirectReplies);
    }

    @Override
    public List<TweetResponseDto> getReposts(Long id) {
        // Validate input
        validateId(id);

        // Read tweet from DB
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet exists
        validateTweet(tweet, id);

        // Filter out inactive (deleted) tweet reposts
        List<Tweet> activeDirectReposts = tweet.getRepostList().stream()
                .filter(repost -> !repost.isDeleted()).toList();

        return tweetMapper.entitiesToResponseDtos(activeDirectReposts);
    }

    @Override
    public List<UserResponseDto> getMentions(Long id) {
        // Validate input
        validateId(id);

        // Read tweet from DB
        Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id);

        // Validate tweet exists
        validateTweet(tweet, id);

        // Filter out inactive (deleted) users
        List<User> activeMentions = tweet.getMentionList().stream()
                .filter(user -> !user.isDeleted()).toList();

        return userMapper.entitiesToDtos(activeMentions);
    }
}

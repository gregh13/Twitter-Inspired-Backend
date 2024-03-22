package com.cooksys.socialmediaassignment.team2.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")  //One user can have many tweets, but a tweet can only have one user
    private User author;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp posted;

    private boolean deleted;

    private String content;

    @ManyToOne
    @JoinColumn(name = "in_reply_to_id") // A tweet can only reply to one tweet, but one tweet can have many direct replies
    private Tweet inReplyTo;

    @ManyToOne
    @JoinColumn(name = "repost_of_id") // A tweet can only be a repost to one tweet, but one tweet can have many reposts
    private Tweet repostOf;

    @OneToMany(mappedBy = "inReplyTo") // One tweet can have many replies
    private List<Tweet> replyList;

    @OneToMany(mappedBy = "repostOf") // One tweet can have many reposts
    private List<Tweet> repostList;

    @ManyToMany // Many-Many --> One tweet can have many tags, and one tag can be used in many tweets
    @JoinTable(
            name = "tweet_hashtags",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> tagList;

    @ManyToMany(mappedBy = "tweetLikeList") // Many-Many --> One tweet can have many likes, and one user can like many tweets
    private List<User> likeList;

    @ManyToMany // Many-Many --> One tweet can have many mentions, and one user can be mentioned in many tweets
    @JoinTable(
            name = "tweet_mentions",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> mentionList;
}

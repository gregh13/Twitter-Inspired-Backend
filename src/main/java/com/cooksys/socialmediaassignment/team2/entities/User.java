package com.cooksys.socialmediaassignment.team2.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@NoArgsConstructor
@Data
public class User {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false)
	@CreationTimestamp
	private Timestamp joined;
	
	private boolean deleted;
	
    @ManyToMany
    @JoinTable(
            name = "followers_following",
            joinColumns = @JoinColumn(name = "following_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id")
    )
    private List<User> followerList;

    @ManyToMany(mappedBy = "followerList")
    private List<User> followingList;
    
    @ManyToMany
    @JoinTable(
            name = "user_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id")
    )
    private List<Tweet> tweetLikeList;
	
	@ManyToMany(mappedBy = "mentionList")
    private List<Tweet> mentionList;
    
	@Embedded
    private Profile profile;
	
    @Embedded
    private Credentials credentials;
    
    @OneToMany(mappedBy = "author")
    private List<Tweet> tweetList;
	
}

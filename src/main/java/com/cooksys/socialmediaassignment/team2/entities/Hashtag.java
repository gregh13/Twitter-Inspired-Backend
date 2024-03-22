package com.cooksys.socialmediaassignment.team2.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Hashtag {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String label;
	
	@Column(nullable = false)
	@CreationTimestamp
	private Timestamp firstUsed; // should be assigned on creation
	
	@Column(nullable = false)
	@UpdateTimestamp
	private Timestamp lastUsed; // should be updated every time a new tweet is tagged with the hashtag
	
	@ManyToMany(mappedBy = "tagList") // Many-Many --> One tweet can have many tags, and one tag can be used in many tweets
	private List<Tweet> tweetList;

}

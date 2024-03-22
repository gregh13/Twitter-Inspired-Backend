package com.cooksys.socialmediaassignment.team2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialmediaassignment.team2.entities.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
	
	// Derived Method to find Hashtags by their name
	Optional<Hashtag> findByLabel(String label);

}

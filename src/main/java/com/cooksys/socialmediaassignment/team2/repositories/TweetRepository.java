package com.cooksys.socialmediaassignment.team2.repositories;

import com.cooksys.socialmediaassignment.team2.entities.Tweet;
import com.cooksys.socialmediaassignment.team2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findAllByDeletedFalseOrderByPostedDesc();

    Tweet findByIdAndDeletedFalse(Long id);

    List<Tweet> findAllByAuthorAndDeletedFalseOrderByPostedDesc(User author);

}

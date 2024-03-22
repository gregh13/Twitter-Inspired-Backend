package com.cooksys.socialmediaassignment.team2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.socialmediaassignment.team2.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	List<User> findAllByDeletedFalse();
	
	Optional<User> findByCredentialsUsername(String username);
	
	Optional<User> findByCredentialsUsernameAndDeletedFalse(String username);

}

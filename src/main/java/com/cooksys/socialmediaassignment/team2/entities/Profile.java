package com.cooksys.socialmediaassignment.team2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Profile {

	private String firstName;
	
	private String lastName;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	private String phone;
	
}

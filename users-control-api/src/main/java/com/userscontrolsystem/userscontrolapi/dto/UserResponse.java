package com.userscontrolsystem.userscontrolapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {

	private Long id;
	private String name;
	private String login;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private String email;
	private Boolean isAdmin;
	
}

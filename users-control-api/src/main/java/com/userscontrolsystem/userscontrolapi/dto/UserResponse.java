package com.userscontrolsystem.userscontrolapi.dto;

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
	private String createdDate;
	private String updatedDate;
	private String email;
	private Boolean isAdmin;
	
}

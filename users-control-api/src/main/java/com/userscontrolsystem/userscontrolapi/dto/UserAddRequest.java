package com.userscontrolsystem.userscontrolapi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserAddRequest {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String login;
	
	@NotBlank
	private String password;
	
	@NotBlank
	private String email;
	
	@NotNull
	private Boolean isAdmin;

}

package com.userscontrolsystem.userscontrolapi.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@NotNull
public class AuthenticateRequest {
	
	@NotNull
	private String login;
	
	@NotNull
	private String password;

}

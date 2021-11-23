package com.userscontrolsystem.userscontrolapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthenticateResponse {
	
	private String login;
	private Boolean isAdmin;
	private String token;

}

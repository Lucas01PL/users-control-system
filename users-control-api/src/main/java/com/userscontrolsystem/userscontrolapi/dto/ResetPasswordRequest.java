package com.userscontrolsystem.userscontrolapi.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	
	@NotNull
	private String newPassword;

}

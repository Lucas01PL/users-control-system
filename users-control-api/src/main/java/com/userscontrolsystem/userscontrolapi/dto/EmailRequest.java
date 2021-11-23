package com.userscontrolsystem.userscontrolapi.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EmailRequest {
	
	@NotBlank
	private String sender;
	
	@NotBlank
	private String recipient;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String message;

}

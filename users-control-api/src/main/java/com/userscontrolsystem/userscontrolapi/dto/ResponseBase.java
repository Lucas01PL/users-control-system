package com.userscontrolsystem.userscontrolapi.dto;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class ResponseBase {

	private Integer status;
	private OffsetDateTime datetime;
	private String title;
	private Object data;
}
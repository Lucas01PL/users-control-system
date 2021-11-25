package com.userscontrolsystem.userscontrolapi.controller;

import javax.validation.Valid;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userscontrolsystem.userscontrolapi.config.RabbitMQConfiguration;
import com.userscontrolsystem.userscontrolapi.dto.EmailRequest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	RabbitMQConfiguration rabbitMQConfiguration;
	
	@ApiOperation(value = "Endpoint to Admin User send email to the User")
	@ApiResponses(value = {
	     @ApiResponse(code = 200, message = "Ok"),
	     @ApiResponse(code = 400, message = "Bad Request"),
	     @ApiResponse(code = 401, message = "Not authorized"),
	     @ApiResponse(code = 403, message = "Forbidden"),
	     @ApiResponse(code = 404, message = "The resource not found"),
	     @ApiResponse(code = 500, message = "Interval Server Error")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "Authorization", value = "", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyWF0IjoxNjM3Nzk1MjU4fQ.2_uzMC2ZbNOf6uWbK8zv")
	})
	@PostMapping
	public ResponseEntity<?> send(@Valid @RequestBody EmailRequest request) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		log.info("Sending message...");
		rabbitTemplate.convertAndSend(rabbitMQConfiguration.getExchangeName(), "", json);
		log.info("Sending message finished.");
		return ResponseEntity.ok().build();
	}
	
}
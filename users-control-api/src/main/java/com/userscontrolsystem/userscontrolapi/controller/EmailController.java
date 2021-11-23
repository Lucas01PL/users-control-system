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

@RestController
@RequestMapping("/email")
public class EmailController {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@PostMapping
	public ResponseEntity<?> send(@Valid @RequestBody EmailRequest request) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Sending message...");
		System.out.println(json);
		rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, "", json);
		System.out.println("Sending message finished.");
		return ResponseEntity.ok().build();
	}
	
}
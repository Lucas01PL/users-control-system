package com.userscontrolsystem.userscontrolmessage.listener;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userscontrolsystem.userscontrolmessage.service.EmailService;
import com.userscontrolsystem.userscontrolmessage.util.UserAPIClient;

@Component
public class KafkaTopicListener {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserAPIClient userApiClient;

    @SuppressWarnings("unchecked")
    @KafkaListener(topics = "${kafka.consumer.topic.name}", groupId = "group_id")
    public void consumer(ConsumerRecord<String, String> payload){
    	try {
    		System.out.println("Message: " + payload.value());
	    	ObjectMapper objectMapper = new ObjectMapper();
			Map<String,?> mapJson = objectMapper.readValue(payload.value(), Map.class);
	    	
	    	enviarEmailAdmin(userApiClient.getAdminUsers());
	    	enviarEmail(mapJson);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void enviarEmailAdmin(List<Map<String,?>> users) {
		for (Map<String,?> user : users) {
			enviarEmail(user);
		}
	}

	private void enviarEmail(Map<String,?> user) {
		String sender = String.valueOf(user.get("recipient"));
		String title = String.valueOf(user.get("title"));
		String body = String.valueOf(user.get("body"));
		emailService.send(sender, title, body);
	}
    
}
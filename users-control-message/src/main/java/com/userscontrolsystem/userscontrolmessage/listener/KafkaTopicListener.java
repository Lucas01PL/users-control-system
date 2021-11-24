package com.userscontrolsystem.userscontrolmessage.listener;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userscontrolsystem.userscontrolmessage.service.EmailService;

@Component
public class KafkaTopicListener {

    @Value("${topic.name.consumer")
    private String topicName;
    
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "${kafka.consumer.topic.name}", groupId = "group_id")
    public void consumer(ConsumerRecord<String, String> payload){
    	System.out.println("Tópico: " + topicName);
    	System.out.println("key: " + payload.key());
    	System.out.println("Headers: " + payload.headers());
    	System.out.println("Partion: " + payload.partition());
    	System.out.println("Order: " + payload.value());
    	
    	try {
	    	ObjectMapper objectMapper = new ObjectMapper();
	    	Map<?,?> mapJson = objectMapper.readValue(payload.value(), Map.class);
	    	enviarEmail(mapJson);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void enviarEmailAdmin(List<Map> users) {
		for (Map user : users) {
			enviarEmail(user);
		}
	}

	private void enviarEmail(Map<?,?> user) {
		String sender = String.valueOf(user.get("recipient"));
		String title = String.valueOf(user.get("title"));
		String body = String.valueOf(user.get("body"));
		emailService.send(sender, title, body);
	}
    
}
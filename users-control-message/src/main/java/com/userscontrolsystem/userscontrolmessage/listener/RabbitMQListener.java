package com.userscontrolsystem.userscontrolmessage.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.userscontrolsystem.userscontrolmessage.config.RabbitMQConfig;
import com.userscontrolsystem.userscontrolmessage.service.KafkaProducerService;

@Component
public class RabbitMQListener {
  
	@Autowired
	private KafkaProducerService kafkaProducerService;
	
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumer(String message) {
        kafkaProducerService.send(message);
    }
}
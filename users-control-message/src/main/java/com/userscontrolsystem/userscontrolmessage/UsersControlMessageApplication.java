package com.userscontrolsystem.userscontrolmessage;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableRabbit
@SpringBootApplication
public class UsersControlMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersControlMessageApplication.class, args);
	}

}

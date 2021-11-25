package com.userscontrolsystem.userscontrolconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class UsersControlConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersControlConfigServerApplication.class, args);
	}

}

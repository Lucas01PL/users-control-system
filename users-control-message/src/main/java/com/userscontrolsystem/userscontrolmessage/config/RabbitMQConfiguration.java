package com.userscontrolsystem.userscontrolmessage.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class RabbitMQConfiguration {
	
	@Value("${rabbitmq.email.exchange}")
	private static String exchangeName;
	
	@Value("${rabbitmq.email.queue}")
	private String queueName;
	
	@Value("${rabbitmq.email.host}")
	private String host;
	
	@Value("${rabbitmq.email.port}")
	private Integer port;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		return connectionFactory;
	}

    @Bean
    public DirectExchange exchange() {
        return ExchangeBuilder.directExchange(exchangeName)
                .durable(true)
                .build();
    }
    
    @Bean
    public Queue queue() {
       return new Queue(queueName, false);
    }
    
    @Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("");
	}
    
    public static String getExchangeName() {
    	return exchangeName;
    }
}
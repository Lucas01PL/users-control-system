package com.userscontrolsystem.userscontrolmessage.config;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {
	
	@Value("${kafka.consumer.servers}")
	private String serversConsumer;
	
	@Value("${kafka.consumer.group.id}")
	private String groupIdConsumer;
	
	@Value("${kafka.consumer.auto.offset.reset}")
	private String autoOffsetResetConsumer;
	
	@Value("${kafka.consumer.key.deserializer.class}")
	private String keyDeserializerConsumer;
	
	@Value("${kafka.consumer.value.deserializer.class}")
	private String valueDeserializerConsumer;
	
	@Value("${kafka.producer.servers}")
	private String serversProducer;
	
	@Value("${kafka.producer.key.serializer.class}")
	private String keySerializerProducer;
	
	@Value("${kafka.producer.value.serializer.class}")
	private String valueSerializerProducer;
	
	@Bean
	public KafkaConsumer<String, String> kafkaConsumer() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serversConsumer);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdConsumer);
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConsumer);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerConsumer);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerConsumer);
		return new KafkaConsumer<String, String>(properties);
	}
	
	@Bean
	public KafkaProducer<String, String> kafkaProducer() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serversProducer);
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerProducer);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerProducer);
		return new KafkaProducer<String, String>(properties);
	}

}

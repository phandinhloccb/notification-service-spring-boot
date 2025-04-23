package com.loc.microservices.notification_service_spring_boot;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@TestConfiguration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "tests.containers.enabled", havingValue = "true", matchIfMissing = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	KafkaContainer kafkaContainer() {
		return new KafkaContainer(DockerImageName.parse("apache/kafka-native:latest"));
	}

}

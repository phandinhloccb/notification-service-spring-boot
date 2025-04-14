package com.loc.microservices.notification_service_spring_boot;

import org.springframework.boot.SpringApplication;

public class TestNotificationServiceSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.from(NotificationServiceSpringBootApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

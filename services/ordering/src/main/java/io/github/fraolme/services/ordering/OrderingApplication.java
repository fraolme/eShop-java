package io.github.fraolme.services.ordering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

// we need to add basePackage stuff to make spring look for our entities, repositories, configurations in our event-bus-rabbitmq library
@SpringBootApplication(scanBasePackages = "io.github.fraolme")
@EnableJpaRepositories(basePackages = {"io.github.fraolme"})
@EntityScan(basePackages = {"io.github.fraolme"})
@EnableScheduling
public class OrderingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingApplication.class, args);
	}

}

package com.roadmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RoadmakerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoadmakerApplication.class, args);
	}
}

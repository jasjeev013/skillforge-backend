package com.skillforge.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SkillForgePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillForgePlatformApplication.class, args);
	}

}

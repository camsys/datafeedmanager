package com.camsys.datafeedmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EnableJpaRepositories("com.camsys.datafeedmanager.repository")
public class DatafeedmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatafeedmanagerApplication.class, args);
	}

}

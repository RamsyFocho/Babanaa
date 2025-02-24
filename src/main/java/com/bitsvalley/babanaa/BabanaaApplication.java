package com.bitsvalley.babanaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")

public class BabanaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabanaaApplication.class, args);
	}

}

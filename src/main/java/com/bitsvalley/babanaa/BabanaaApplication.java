package com.bitsvalley.babanaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class BabanaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabanaaApplication.class, args);
	}

}

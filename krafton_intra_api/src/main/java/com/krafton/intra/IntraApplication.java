package com.krafton.intra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class IntraApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntraApplication.class, args);
	}

}

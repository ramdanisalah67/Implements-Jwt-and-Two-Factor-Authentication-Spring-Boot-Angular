package com.alibou.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {

		SpringApplication.run(SecurityApplication.class, args);

	}

	@Bean
public	CommandLineRunner commandLineRunner(){
		return args->{
			// Generate a 256-bit (32-byte) random secret key
			byte[] secretKeyBytes = new byte[32];
			new SecureRandom().nextBytes(secretKeyBytes);

			// Convert the byte array to a Base64-encoded string
			String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);
			System.out.println("Generated Secret Key: " + secretKey);
		};
	}

}

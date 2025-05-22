package com.g4bzz.linkurto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LinkurtoApplication {
	public static void main(String[] args) {
		SpringApplication.run(LinkurtoApplication.class, args);
	}
}

package com.example.roenbeauty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RoenBeautyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoenBeautyApplication.class, args);
	}

}

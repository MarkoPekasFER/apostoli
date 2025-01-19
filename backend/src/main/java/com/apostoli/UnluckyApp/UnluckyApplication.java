package com.apostoli.UnluckyApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UnluckyApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnluckyApplication.class, args);
	}

}

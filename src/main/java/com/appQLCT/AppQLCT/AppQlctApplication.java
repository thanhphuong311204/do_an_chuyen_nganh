package com.appQLCT.AppQLCT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.appQLCT.AppQLCT", "com.appQLCT.security"})
public class AppQlctApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppQlctApplication.class, args);
	}

}

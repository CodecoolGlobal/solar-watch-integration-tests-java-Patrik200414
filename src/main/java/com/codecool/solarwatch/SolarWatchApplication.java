package com.codecool.solarwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Prpushofile("!test")
@SpringBootApplication
public class SolarWatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolarWatchApplication.class, args);
	}

}

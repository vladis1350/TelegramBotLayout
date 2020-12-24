package com.example.aducarBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;


@SpringBootApplication
//@EnableScheduling
public class FitnessMarathonBotApplication {

    public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(FitnessMarathonBotApplication.class, args);
	}

}

package com.skypro.ShelterPetTelegramBot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
@EnableCaching
public class ShelterPetTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShelterPetTelegramBotApplication.class, args);
    }

}
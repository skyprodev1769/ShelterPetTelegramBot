package com.skypro.ShelterPetTelegramBot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class ShelterPetTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShelterPetTelegramBotApplication.class, args);
    }

}
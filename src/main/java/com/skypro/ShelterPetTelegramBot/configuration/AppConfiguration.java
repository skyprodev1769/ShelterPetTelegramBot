package com.skypro.ShelterPetTelegramBot.configuration;

import com.skypro.ShelterPetTelegramBot.service.TelegramBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.regex.Pattern;

/**
 * Класс {@link AppConfiguration}
 * является конфигурационным классом для работы телеграм бота {@link TelegramBot}
 */
@Data
@Configuration
@PropertySource("application.properties")
public class AppConfiguration {

    private Boolean isDogShelter = true;

    private Pattern pattern = Pattern.compile("([\\W+]+)(\\s)([\\W+]+)(\\s)([0-9]{11})");

    @Value("${shelters.path-to-dog-shelter}")
    private String pathForDogShelter;

    @Value("${shelters.path-to-cat-shelter}")
    private String pathForCatShelter;

    @Value("${pets.path-to-photo}")
    private String photosDir;
}
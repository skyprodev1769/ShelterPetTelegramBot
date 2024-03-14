package com.skypro.ShelterPetTelegramBot.configuration;

import com.skypro.ShelterPetTelegramBot.service.TelegramBot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Класс {@link BotConfiguration}
 * является конфигурационным классом для телеграм бота {@link TelegramBot}
 */
@Data
@Configuration
@PropertySource("application.properties")
public class BotConfiguration {

    @Value("${bot.name}")
    private String name;

    @Value("${bot.token}")
    private String token;
}
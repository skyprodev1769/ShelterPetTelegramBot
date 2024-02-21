package com.skypro.ShelterPetTelegramBot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Класс {@link BotConfiguration}
 * является конфигурационным классом для телеграм бота и имеет 2 поля: <br> <br>
 *
 * <b> {@code String name} </b> <br> <i> является наименованием телеграм бота </i> <br> <br>
 * <b> {@code String token} </b> <br> <i> является аутентификационным номером телеграм бота </i>
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
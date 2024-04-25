package com.skypro.ShelterPetTelegramBot.configuration;

import com.skypro.ShelterPetTelegramBot.service.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс {@link BotInitializer}
 * является конфигурационным классом для регистрации телеграм бота {@link TelegramBot}
 */
@Slf4j
@Component
public class BotInitializer {

    @Autowired
    TelegramBot telegramBot;

    /**
     * Метод регистрирует нового телеграм бота {@link TelegramBot}
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА РЕГИСТРАЦИИ БОТА: {}", e.getMessage());
        }
    }
}
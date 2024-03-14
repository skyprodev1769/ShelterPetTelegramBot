package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.WorkInBotImpl;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Интерфейс {@link WorkInBot}
 * содержит набор методов для сервисного класса {@link WorkInBotImpl}
 */
public interface WorkInBot {

    /**
     * Метод содержит логику работы телеграм бота
     *
     * @param update <i> является получаемым от пользователя сообщением </i>
     */
    void work(Update update);
}
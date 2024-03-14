package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.SendingNotificationsImpl;

/**
 * Интерфейс {@link SendingNotifications}
 * содержит набор методов для сервисного класса {@link SendingNotificationsImpl}
 */
public interface SendingNotifications {

    /**
     * Метод отправляет напоминание усыновителю
     */
    void sendReminder();

    /**
     * Метод отправляет предупреждение усыновителю
     */
    void sendWarning();
}
package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.RecordingContactsImpl;

import java.util.regex.Matcher;

/**
 * Интерфейс {@link RecordingContacts}
 * содержит набор методов для сервисного класса {@link RecordingContactsImpl}
 */
public interface RecordingContacts {

    /**
     * Метод записывает контактные данные потенциального усыновителя {@link PotentialParent}
     *
     * @param chatId  <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param matcher <i> является объектом класса {@link Matcher}</i>
     */
    PotentialParent recordContact(Long chatId, Matcher matcher);
}
package com.skypro.ShelterPetTelegramBot.service.interfaces;

import com.skypro.ShelterPetTelegramBot.model.entity.PotentialParent;
import com.skypro.ShelterPetTelegramBot.service.impl.RecordContactsImpl;

import java.util.regex.Matcher;

/**
 * Интерфейс {@link RecordContacts}
 * содержит набор методов для класса {@link RecordContactsImpl}
 */
public interface RecordContacts {

    /**
     * Метод записывает контактные данные потенциального усыновителя {@link PotentialParent}
     *
     * @param chatId  <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param matcher <i> является объектом класса {@link Matcher}</i>
     */
    PotentialParent recordContact(Long chatId, Matcher matcher);
}
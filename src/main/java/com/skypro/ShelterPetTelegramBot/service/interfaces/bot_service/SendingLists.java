package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.SendingListsImpl;

/**
 * Интерфейс {@link SendingLists}
 * содержит набор методов для сервисного класса {@link SendingListsImpl}
 */
public interface SendingLists {

    /**
     * Метод возвращает, в зависимости от выбранного приюта, список животных для усыновления из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param isDogShelter  <i> является условием выбранного приюта </i>
     */
    StringBuilder sendListPets(Long chatId,
                               String userFirstName,
                               Boolean isDogShelter);

    /**
     * Метод возвращает список контактов волонтеров из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    StringBuilder sendListVolunteers(Long chatId, String userFirstName);

    /**
     * Метод возвращает список адресов приютов из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param isDogShelter  <i> является условием выбранного приюта </i>
     */
    StringBuilder sendListShelters(Long chatId,
                                   String userFirstName,
                                   Boolean isDogShelter);
}
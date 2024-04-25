package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.GetReactionsImpl;

/**
 * Интерфейс {@link GetReactions}
 * содержит набор методов для сервисного класса {@link GetReactionsImpl}
 */
public interface GetReactions {

    /**
     * Метод возвращает реакции на команды для <b> НЕЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param text          <i> является полученным текстом от пользователя </i>
     */
    void getReactionsForUnregisteredUsers(Long chatId, String userFirstName, String text);

    /**
     * Метод возвращает реакции на команды для <b> ЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param text          <i> является полученным текстом от пользователя </i>
     */
    void getReactionsForRegisteredUsers(Long chatId, String userFirstName, String text);

    /**
     * Метод возвращает реакции на нажатие кнопок пользователями
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param callbackData  <i> является данными для идентификации нажатой кнопки </i>
     */
    void getReactionsToClickingButtons(Long chatId, String userFirstName, String callbackData);
}
package com.skypro.ShelterPetTelegramBot.service.interfaces;

import com.skypro.ShelterPetTelegramBot.service.impl.CreatingKeyBoardsImpl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Интерфейс {@link CreatingKeyBoards}
 * содержит набор методов для класса {@link CreatingKeyBoardsImpl}
 */
public interface CreatingKeyBoards {

    /**
     * Метод создает интерактивную клавиатуру для выбора команд для <b> НЕЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForUnregisteredUsers(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для выбора приюта
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForChoiceShelter(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для выбора основных шагов запросов информации о приюте <b> ДЛЯ СОБАК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardGeneralStepsForDogShelter(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для выбора основных шагов запросов информации о приюте <b> ДЛЯ КОШЕК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardGeneralStepsForCatShelter(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для получения подробной информации о приюте <b> ДЛЯ СОБАК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForDetailedInfoAboutDogShelter(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для получения подробной информации о приюте <b> ДЛЯ КОШЕК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForDetailedInfoAboutCatShelter(Long chatId, String answer);
}
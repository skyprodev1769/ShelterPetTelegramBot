package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.CreatingKeyBoardsImpl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Интерфейс {@link CreatingKeyBoards}
 * содержит набор методов для сервисного класса {@link CreatingKeyBoardsImpl}
 */
public interface CreatingKeyBoards {

    /**
     * Метод создает интерактивную клавиатуру для регистрации нового пользователя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForRegistration(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для выбора приюта
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForChoiceShelter(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для выбора общих запросов информации
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForGeneralInfo(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для получения подробной информации о приюте
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForDetailedInfoAboutShelter(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для получения подробной информации о процессе получения <b> СОБАКИ </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForDetailedInfoAboutProcessForDog(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для получения подробной информации о процессе получения <b> КОШКИ </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForDetailedInfoAboutProcessForCat(Long chatId, String answer);

    /**
     * Метод создает интерактивную клавиатуру для получения информации об отправке отчета о животном
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createKeyBoardForReportPet(Long chatId, String answer);
}
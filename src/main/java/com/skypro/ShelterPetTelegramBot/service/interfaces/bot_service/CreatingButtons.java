package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.CreatingButtonsImpl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Интерфейс {@link CreatingButtons}
 * содержит набор методов для сервисного класса {@link CreatingButtonsImpl}
 */
public interface CreatingButtons {

    /**
     * Метод создает кнопки под сообщением для выбора ответа на регистрацию нового пользователя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonsForChoiceRegistration(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для регистрации нового пользователя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonForRegistration(Long chatId, String answer);

    /**
     * Метод создает кнопки под сообщением для выбора приюта
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonsForChoiceShelter(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для получения схемы проезда к приюту
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonForSchemeDrivingToShelter(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для удаления контактных данных усыновителя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonForRemoveContactDetails(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для вызова волонтера
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonForCallVolunteer(Long chatId, String answer);
}
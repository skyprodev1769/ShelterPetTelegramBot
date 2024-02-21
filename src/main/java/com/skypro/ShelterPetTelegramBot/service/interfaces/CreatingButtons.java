package com.skypro.ShelterPetTelegramBot.service.interfaces;

import com.skypro.ShelterPetTelegramBot.service.impl.CreatingButtonsImpl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Интерфейс {@link CreatingButtons}
 * содержит набор методов для класса {@link CreatingButtonsImpl}
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
     * Метод создает кнопку под сообщением для получения схемы проезда к приюту <b> ДЛЯ СОБАК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonsForSchemeDrivingForDogShelter(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для получения схемы проезда к приюту <b> ДЛЯ КОШЕК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonsForSchemeDrivingForCatShelter(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для выбора детальной информации о приюте <b> ДЛЯ СОБАК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonsForGetDetailedInfoAboutDogShelter(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для выбора детальной информации о приюте <b> ДЛЯ КОШЕК </b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonsForGetDetailedInfoAboutCatShelter(Long chatId, String answer);

    /**
     * Метод создает кнопку под сообщением для вызова волонтера
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    SendMessage createButtonForCallVolunteer(Long chatId, String answer);
}
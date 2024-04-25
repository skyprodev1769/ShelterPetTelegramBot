package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.model.enums.MessageContent;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.SendingMessageImpl;

/**
 * Интерфейс {@link SendingMessage}
 * содержит набор методов для сервисного класса {@link SendingMessageImpl}
 */
public interface SendingMessage {

    /**
     * Метод отправляет ответ волонтера усыновителю
     *
     * @param content     <i> является определителем содержания сообщения </i>
     * @param firstName   <i> является именем получателя </i> <br>
     * @param lastName    <i> является фамилией получателя </i> <br>
     * @param phoneNumber <i> является номером телефона получателя </i> <br>
     * @param petName     <i> является именем усыновленного животного получателя </i>
     */
    String sendAnswer(MessageContent content,
                      String firstName,
                      String lastName,
                      String phoneNumber,
                      String petName);
}
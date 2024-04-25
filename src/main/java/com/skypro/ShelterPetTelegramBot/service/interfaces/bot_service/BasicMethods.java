package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.BasicMethodsImpl;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Интерфейс {@link BasicMethods}
 * содержит набор методов для сервисного класса {@link BasicMethodsImpl}
 */
public interface BasicMethods {

    /**
     * Метод является ответной реакцией в виде текстового сообщения на действие пользователя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    void reaction(Long chatId, String answer);

    /**
     * Метод отправляет текстовое сообщение пользователю
     *
     * @param message <i> является объектом класса {@link SendMessage} </i>
     */
    void sendText(SendMessage message);

    /**
     * Метод отправляет найденное изображение пользователю: <br>
     *
     * @param chatId  <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param caption <i> является подписью под картинкой для отправки пользователю </i> <br>
     * @param path    <i> является путем к расположению изображения </i>
     */
    void sendImage(Long chatId, String caption, String path);
}
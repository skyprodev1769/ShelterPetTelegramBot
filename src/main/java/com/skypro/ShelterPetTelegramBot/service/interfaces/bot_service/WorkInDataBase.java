package com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.User;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.WorkInDataBaseImpl;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;
import java.util.regex.Matcher;

/**
 * Интерфейс {@link WorkInDataBase}
 * содержит набор методов для сервисного класса {@link WorkInDataBaseImpl}
 */
public interface WorkInDataBase {

    /**
     * Метод сохраняет нового пользователя {@link User} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    void saveNewUserToDB(Long chatId, String userFirstName);

    /**
     * Метод сохраняет контакты потенциального усыновителя {@link PotentialParent} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param matcher       <i> является объектом класса {@link Matcher} </i>
     */
    void savePotentialParentToDB(Long chatId,
                                 String userFirstName,
                                 Matcher matcher);

    /**
     * Метод удаляет все ранее переданные контакты усыновителя {@link PotentialParent} из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    void removePotentialParentFromDB(Long chatId, String userFirstName);

    /**
     * Метод сохраняет отчет о животном {@link Report} в БД
     *
     * @param parent   <i> является усыновителем, отправившим отчет </i> <br>
     * @param photos   <i> являются фото, отправляемыми усыновителем </i> <br>
     * @param document <i> является документом, отправляемым усыновителем </i>
     */
    void saveReport(Parent parent,
                    List<PhotoSize> photos,
                    Document document,
                    String photosDir);
}
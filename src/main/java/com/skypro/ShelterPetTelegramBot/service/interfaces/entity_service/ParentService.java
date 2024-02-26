package com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.ParentServiceImpl;

import java.util.Collection;

/**
 * Интерфейс {@link ParentService}
 * содержит набор методов для сервисного класса {@link ParentServiceImpl}
 */
public interface ParentService {

    /**
     * Метод добавляет нового усыновителя в БД
     *
     * @param firstName   <i> является именем нового усыновителя </i> <br>
     * @param lastName    <i> является фамилией нового усыновителя </i> <br>
     * @param phoneNumber <i> является номером телефона нового усыновителя </i> <br>
     * @param volunteerId <i> является идентификатором волонтера </i> <br>
     * @param petId       <i> является идентификатором животного </i>
     */
    Parent add(String firstName,
               String lastName,
               String phoneNumber,
               Long volunteerId,
               Long petId);

    /**
     * Метод находит усыновителя из БД по id
     *
     * @param id <i> является идентификатором усыновителя </i>
     */
    Parent getById(Long id);

    /**
     * Метод находит всех усыновителей из БД по разным параметрам
     *
     * @param firstName   <i> является именем усыновителя </i> <br>
     * @param lastName    <i> является фамилией усыновителя </i> <br>
     * @param phoneNumber <i> является номером телефона усыновителя </i> <br>
     * @param volunteerId <i> является идентификатором волонтера </i>
     */
    Collection<Parent> getAllByParameters(String firstName,
                                          String lastName,
                                          String phoneNumber,
                                          Long volunteerId);

    /**
     * Метод находит всех усыновителей из БД
     */
    Collection<Parent> getAll();

    /**
     * Метод изменяет данные усыновителя В БД
     *
     * @param id          <i> является идентификатором усыновителя </i> <br>
     * @param firstName   <i> является новым именем усыновителя </i> <br>
     * @param lastName    <i> является новой фамилией усыновителя </i> <br>
     * @param phoneNumber <i> является новым номером телефона усыновителя </i> <br>
     * @param volunteerId <i> является новым идентификатором волонтера </i> <br>
     * @param petId       <i> является новым идентификатором животного </i>
     */
    Parent edit(Long id,
                String firstName,
                String lastName,
                String phoneNumber,
                Long volunteerId,
                Long petId);

    /**
     * Метод удаляет усыновителя из БД
     *
     * @param id <i> является идентификатором усыновителя </i>
     */
    Parent delete(Long id);
}
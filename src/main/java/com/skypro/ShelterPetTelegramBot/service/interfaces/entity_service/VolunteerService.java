package com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.VolunteerServiceImpl;

import java.util.Collection;

/**
 * Интерфейс {@link VolunteerService}
 * содержит набор методов для сервисного класса {@link VolunteerServiceImpl}
 */
public interface VolunteerService {

    /**
     * Метод добавляет нового волонтера в БД
     *
     * @param firstName   <i> является именем нового волонтера </i> <br>
     * @param lastName    <i> является фамилией нового волонтера </i> <br>
     * @param phoneNumber <i> является номером телефона нового волонтера </i> <br>
     * @param shelterId   <i> является идентификатором приюта для животных </i>
     */
    Volunteer add(String firstName,
                  String lastName,
                  String phoneNumber,
                  Long shelterId);

    /**
     * Метод находит волонтера из БД по id
     *
     * @param id <i> является идентификатором волонтера </i>
     */
    Volunteer getById(Long id);

    /**
     * Метод находит всех волонтеров из БД по разным параметрам
     *
     * @param firstName   <i> является именем волонтера </i> <br>
     * @param lastName    <i> является фамилией волонтера </i> <br>
     * @param phoneNumber <i> является номером телефона волонтера </i> <br>
     * @param shelterId   <i> является идентификатором приюта для животных </i>
     */
    Collection<Volunteer> getAllByParameters(String firstName,
                                             String lastName,
                                             String phoneNumber,
                                             Long shelterId);

    /**
     * Метод находит всех волонтеров из БД
     */
    Collection<Volunteer> getAll();

    /**
     * Метод изменяет данные волонтера В БД по id
     *
     * @param id          <i> является идентификатором волонтера </i> <br>
     * @param firstName   <i> является новым именем волонтера </i> <br>
     * @param lastName    <i> является новой фамилией волонтера </i> <br>
     * @param phoneNumber <i> является новым номером телефона волонтера </i> <br>
     * @param shelterId   <i> является идентификатором нового приюта для животных </i>
     */
    Volunteer edit(Long id,
                   String firstName,
                   String lastName,
                   String phoneNumber,
                   Long shelterId);

    /**
     * Метод удаляет волонтера из БД по id
     *
     * @param id <i> является идентификатором волонтера </i>
     */
    Volunteer delete(Long id);
}
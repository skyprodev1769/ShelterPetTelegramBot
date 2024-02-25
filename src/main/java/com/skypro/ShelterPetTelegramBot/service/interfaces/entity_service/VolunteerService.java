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
     * @param firstName   <i> является именем нового волонтера </i>
     * @param lastName    <i> является фамилией нового волонтера </i>
     * @param phoneNumber <i> является номером телефона нового волонтера </i>
     * @param shelterId   <i> является идентификатором приюта для нового животного </i>
     */
    Volunteer add(String firstName,
                  String lastName,
                  String phoneNumber,
                  Long shelterId);

    /**
     * Метод находит волонтера из БД
     *
     * @param id <i> является идентификатором волонтера </i>
     */
    Volunteer get(Long id);

    /**
     * Метод находит всех волонтеров из БД
     */
    Collection<Volunteer> getAll();

    /**
     * Метод изменяет данные волонтера В БД
     *
     * @param id          <i> является идентификатором волонтера </i> <br>
     * @param firstName   <i> является новым именем волонтера </i>
     * @param lastName    <i> является новой фамилией волонтера </i>
     * @param phoneNumber <i> является новым номером телефона волонтера </i>
     * @param shelterId   <i> является новым идентификатором приюта для животного </i>
     */
    Volunteer edit(Long id,
                   String firstName,
                   String lastName,
                   String phoneNumber,
                   Long shelterId);

    /**
     * Метод удаляет волонтера из БД
     *
     * @param id <i> является идентификатором волонтера </i>
     */
    Volunteer delete(Long id);
}
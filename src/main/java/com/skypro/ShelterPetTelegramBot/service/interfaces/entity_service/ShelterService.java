package com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.ShelterServiceImpl;

import java.util.Collection;

/**
 * Интерфейс {@link ShelterService}
 * содержит набор методов для сервисного класса {@link ShelterServiceImpl}
 */
public interface ShelterService {

    /**
     * Метод добавляет новый приют для животных в БД
     *
     * @param type    <i> является указателем типа животных в приюте </i> <br>
     * @param address <i> является адресом нового приюта </i>
     */
    Shelter add(PetType type, String address);

    /**
     * Метод находит приют для животных из БД по id
     *
     * @param id <i> является идентификатором приюта </i>
     */
    Shelter getById(Long id);

    /**
     * Метод находит все приюты для животных из БД по разным параметрам
     *
     * @param address <i> является адресом приюта </i> <br>
     * @param type    <i> является типом животного </i>
     */
    Collection<Shelter> getAllByParameters(String address, PetType type);

    /**
     * Метод находит все приюты для животных из БД
     */
    Collection<Shelter> getAll();

    /**
     * Метод изменяет данные приюта для животных В БД
     *
     * @param id      <i> является идентификатором приюта </i> <br>
     * @param type    <i> является новым указателем типа животных в приюте </i> <br>
     * @param address <i> является новым адресом приюта </i>
     */
    Shelter edit(Long id, PetType type, String address);

    /**
     * Метод удаляет приют для животных из БД
     *
     * @param id <i> является идентификатором приюта </i>
     */
    Shelter delete(Long id);
}
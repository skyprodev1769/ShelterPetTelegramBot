package com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.PetServiceImpl;

import java.util.Collection;

/**
 * Интерфейс {@link PetService}
 * содержит набор методов для сервисного класса {@link PetServiceImpl}
 */
public interface PetService {

    /**
     * Метод добавляет новое животное в БД
     *
     * @param type      <i> является указателем типа нового животного </i> <br>
     * @param name      <i> является именем нового животного </i> <br>
     * @param status    <i> является статусом животного </i> <br>
     * @param shelterId <i> является идентификатором приюта для нового животного </i>
     */
    Pet add(PetType type,
            String name,
            PetStatus status,
            Long shelterId);

    /**
     * Метод находит животное из БД по id
     *
     * @param id <i> является идентификатором животного </i>
     */
    Pet getById(Long id);

    /**
     * Метод находит всех животных из БД по разным параметрам
     *
     * @param name      <i> является именем животного </i> <br>
     * @param type      <i> является типом животного </i> <br>
     * @param status    <i> является статусом животного </i> <br>
     * @param shelterId <i> является идентификатором приюта</i>
     */
    Collection<Pet> getAllByParameters(String name,
                                       PetType type,
                                       PetStatus status,
                                       Long shelterId);

    /**
     * Метод находит всех животных из БД
     */
    Collection<Pet> getAll();

    /**
     * Метод изменяет данные животного В БД
     *
     * @param id        <i> является идентификатором животного </i> <br>
     * @param type      <i> является новым указателем типа животного </i> <br>
     * @param name      <i> является новым именем животного </i> <br>
     * @param status    <i> является статусом животного </i> <br>
     * @param shelterId <i> является новым идентификатором приюта для животного </i>
     */
    Pet edit(Long id,
             PetType type,
             String name,
             PetStatus status,
             Long shelterId);

    /**
     * Метод удаляет животное из БД
     *
     * @param id <i> является идентификатором животного </i>
     */
    Pet delete(Long id);
}
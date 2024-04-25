package com.skypro.ShelterPetTelegramBot.service.interfaces;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import com.skypro.ShelterPetTelegramBot.service.impl.CheckServiceImpl;

import java.util.Collection;

/**
 * Интерфейс {@link CheckService}
 * содержит набор методов для сервисного класса {@link CheckServiceImpl}
 */
public interface CheckService {

    /**
     * Метод проверяет вводимый статус животного
     *
     * @param status <i> является проверяемым статусом животного </i>
     */
    boolean checkStatus(PetStatus status);

    /**
     * Метод проверяет вводимый тип животного на соответствие
     *
     * @param typeOne <i> является проверяемым типом животного </i> <br>
     * @param typeTwo <i> является проверяющим типом приюта для животных </i>
     */
    boolean checkTypes(PetType typeOne, PetType typeTwo);

    /**
     * Метод изменяет переданный номер телефона под заданный формат
     *
     * @param phoneNumber <i> является изменяемым номером телефона </i>
     */
    String validatePhoneNumber(String phoneNumber);

    /**
     * Метод проверяет вводимый номер телефона на корректность
     *
     * @param phoneNumber <i> является проверяемым номером телефона </i>
     */
    boolean checkPhoneNumber(String phoneNumber);

    /**
     * Метод проверяет вводимое имя на корректность
     *
     * @param name <i> является проверяемым именем </i>
     */
    boolean checkName(String name);

    /**
     * Метод проверяет вводимый адрес на корректность
     *
     * @param address <i> является проверяемым адресом </i>
     */
    boolean checkAddress(String address);

    /**
     * Метод проверяет вводимое значение на корректность
     *
     * @param value <i> является проверяемым значением </i>
     */
    boolean checkValue(Long value);

    /**
     * Метод проверяет вводимый номер телефона усыновителя на наличие идентичной записи в БД
     *
     * @param parents <i> является списком усыновителей </i> <br>
     * @param parent  <i> является проверяемым усыновителем </i>
     */
    boolean checkParentAlreadyAdded(Collection<Parent> parents, Parent parent);

    /**
     * Метод проверяет вводимый номер телефона волонтера на наличие идентичной записи в БД
     *
     * @param volunteers <i> является списком волонтеров </i> <br>
     * @param volunteer  <i> является проверяемым волонтером </i>
     */
    boolean checkVolunteerAlreadyAdded(Collection<Volunteer> volunteers, Volunteer volunteer);

    /**
     * Метод проверяет животное на наличие идентичной записи в БД
     *
     * @param pets <i> является списком всех животных </i> <br>
     * @param pet  <i> является проверяемым животным </i>
     */
    boolean checkPetAlreadyAdded(Collection<Pet> pets, Pet pet);

    /**
     * Метод проверяет приют на наличие идентичной записи в БД
     *
     * @param shelters <i> является списком всех приютов </i> <br>
     * @param shelter  <i> является проверяемым приютом </i>
     */
    boolean checkShelterAlreadyAdded(Collection<Shelter> shelters, Shelter shelter);
}
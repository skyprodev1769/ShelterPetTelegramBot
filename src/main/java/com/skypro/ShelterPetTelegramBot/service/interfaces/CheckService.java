package com.skypro.ShelterPetTelegramBot.service.interfaces;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.service.impl.CheckServiceImpl;

import java.util.Collection;

/**
 * Интерфейс {@link CheckService}
 * содержит набор методов для сервисного класса {@link CheckServiceImpl}
 */
public interface CheckService {

    /**
     * Метод выполняет комплексную проверку имени и фамилии
     *
     * @param firstName <i> является проверяемым именем </i> <br>
     * @param lastName  <i> является проверяемым именем </i>
     */
    void checkFullName(String firstName,
                       String lastName);

    /**
     * Метод выполняет комплексную проверку животного
     *
     * @param typeOne <i> является проверяемым типом животного </i> <br>
     * @param typeTwo <i> является проверяющим типом приюта для животных </i> <br>
     * @param name    <i> является проверяемым именем </i> <br>
     * @param pet     <i> является проверяемым животным </i> <br>
     * @param pets    <i> является списком всех животных </i>
     */
    void checkPet(PetType typeOne,
                  PetType typeTwo,
                  String name,
                  Pet pet,
                  Collection<Pet> pets);

    /**
     * Метод выполняет комплексную проверку приюта
     *
     * @param address  <i> является проверяемым адресом </i> <br>
     * @param shelter  <i> является проверяемым приютом </i> <br>
     * @param shelters <i> является списком всех приютов </i>
     */
    void checkShelter(String address,
                      Shelter shelter,
                      Collection<Shelter> shelters);

    /**
     * Метод проверяет вводимое значение на корректность
     *
     * @param value <i> является проверяемым значением </i>
     */
    void checkValue(Long value);

    /**
     * Метод проверяет вводимое имя на корректность
     *
     * @param name <i> является проверяемым именем </i>
     */
    void checkName(String name);

    /**
     * Метод проверяет вводимый адрес на корректность
     *
     * @param address <i> является проверяемым адресом </i>
     */
    void checkAddress(String address);

    /**
     * Метод изменяет переданный номер телефона под заданный формат
     *
     * @param phoneNumber <i> является проверяемым номером телефона </i>
     */
    String validatePhoneNumber(String phoneNumber);

    /**
     * Метод проверяет вводимый номер телефона на корректность
     *
     * @param phoneNumber <i> является проверяемым номером телефона </i>
     */
    void checkPhoneNumber(String phoneNumber);

    /**
     * Метод проверяет вводимый статус животного
     *
     * @param status <i> является проверяемым статусом животного </i>
     */
    void checkStatus(PetStatus status);

    /**
     * Метод проверяет вводимый тип животного на соответствие
     *
     * @param typeOne <i> является проверяемым типом животного </i> <br>
     * @param typeTwo <i> является проверяющим типом приюта для животных </i>
     */
    void checkTypes(PetType typeOne, PetType typeTwo);

    /**
     * Метод проверяет вводимый номер телефона усыновителя на наличие идентичной записи в БД
     *
     * @param parents     <i> является списком усыновителей </i> <br>
     * @param phoneNumber <i> является проверяемым номером телефона </i>
     */
    void checkPhoneNumberParentAlreadyAdded(Collection<Parent> parents, String phoneNumber);

    /**
     * Метод проверяет вводимый номер телефона волонтера на наличие идентичной записи в БД
     *
     * @param volunteers  <i> является списком волонтеров </i> <br>
     * @param phoneNumber <i> является проверяемым номером телефона </i>
     */
    void checkPhoneNumberVolunteerAlreadyAdded(Collection<Volunteer> volunteers, String phoneNumber);

    /**
     * Метод проверяет приют на наличие идентичной записи в БД
     *
     * @param shelters <i> является списком всех приютов </i> <br>
     * @param shelter  <i> является проверяемым приютом </i>
     */

    void checkShelterAlreadyAdded(Collection<Shelter> shelters, Shelter shelter);

    /**
     * Метод проверяет животное на наличие идентичной записи в БД
     *
     * @param pets <i> является списком всех животных </i> <br>
     * @param pet  <i> является проверяемым животным </i>
     */
    void checkPetAlreadyAdded(Collection<Pet> pets, Pet pet);
}
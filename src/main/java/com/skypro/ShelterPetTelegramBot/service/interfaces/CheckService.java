package com.skypro.ShelterPetTelegramBot.service.interfaces;

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
     * Метод выполняет комплексную проверку усыновителя
     */
    void checkParent(String firstName,
                     String lastName,
                     String phoneNumber,
                     PetType typeOne,
                     PetType typeTwo,
                     Parent parent,
                     Collection<Parent> parents);

    /**
     * Метод выполняет комплексную проверку волонтера
     */
    void checkVolunteer(String firstName,
                        String lastName,
                        String phoneNumber,
                        Volunteer volunteer,
                        Collection<Volunteer> volunteers);

    /**
     * Метод выполняет комплексную проверку животного
     */
    void checkPet(PetType typeOne,
                  PetType typeTwo,
                  String name,
                  Pet pet,
                  Collection<Pet> pets);

    /**
     * Метод выполняет комплексную проверку приюта
     */
    void checkShelter(String address,
                      Shelter shelter,
                      Collection<Shelter> shelters);

    /**
     * Метод проверяет вводимое число на корректность
     *
     * @param value <i> является проверяемым значением </i>
     */
    void validateLong(Long value);

    /**
     * Метод проверяет вводимое имя на корректность
     *
     * @param string <i> является проверяемым значением </i>
     */
    void validateName(String string);

    /**
     * Метод проверяет вводимый адрес на корректность
     *
     * @param string <i> является проверяемым значением </i>
     */
    void validateAddress(String string);

    /**
     * Метод проверяет вводимый номер на корректность
     *
     * @param phoneNumber <i> является проверяемым номером </i>
     */
    void validatePhoneNumber(String phoneNumber);

    /**
     * Метод проверяет вводимый тип животного на соответствие
     *
     * @param typeOne <i> является проверяемым типы </i> <br>
     * @param typeTwo <i> является проверяющим типом </i>
     */
    void validateTypes(PetType typeOne, PetType typeTwo);

    /**
     * Метод проверяет вводимый номер телефона усыновителя на наличие идентичной записи в БД
     *
     * @param parents     <i> является списком усыновителей </i> <br>
     * @param phoneNumber <i> является проверяемым номером телефона </i>
     */
    void isPhoneNumberParentAlreadyAdded(Collection<Parent> parents, String phoneNumber);

    /**
     * Метод проверяет вводимый номер телефона волонтера на наличие идентичной записи в БД
     *
     * @param volunteers  <i> является списком волонтеров </i> <br>
     * @param phoneNumber <i> является проверяемым номером телефона </i>
     */
    void isPhoneNumberVolunteerAlreadyAdded(Collection<Volunteer> volunteers, String phoneNumber);

    /**
     * Метод проверяет приют на наличие идентичной записи в БД
     *
     * @param shelters <i> является списком всех приютов </i> <br>
     * @param shelter  <i> является проверяемым приютом </i>
     */

    void isShelterAlreadyAdded(Collection<Shelter> shelters, Shelter shelter);

    /**
     * Метод проверяет волонтера на наличие идентичной записи в БД
     *
     * @param volunteers <i> является списком всех волонтеров </i> <br>
     * @param volunteer  <i> является проверяемым волонтером </i>
     */
    void isVolunteerAlreadyAdded(Collection<Volunteer> volunteers, Volunteer volunteer);

    /**
     * Метод проверяет животное на наличие идентичной записи в БД
     *
     * @param pets <i> является списком всех животных </i> <br>
     * @param pet  <i> является проверяемым животным </i>
     */
    void isPetAlreadyAdded(Collection<Pet> pets, Pet pet);

    /**
     * Метод проверяет усыновителя на наличие идентичной записи в БД
     *
     * @param parents <i> является списком всех усыновителей </i> <br>
     * @param parent  <i> является проверяемым усыновителем </i>
     */
    void isParentAlreadyAdded(Collection<Parent> parents, Parent parent);
}
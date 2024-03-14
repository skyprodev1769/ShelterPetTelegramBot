package com.skypro.ShelterPetTelegramBot.service.impl;

import com.skypro.ShelterPetTelegramBot.exception.DifferentTypesException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideNumberException;
import com.skypro.ShelterPetTelegramBot.exception.PetStatusException;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;

/**
 * Класс {@link CheckServiceImpl}
 * является сервисным классом для проверок
 */
@Slf4j
@Service
public class CheckServiceImpl implements CheckService {

    @Override
    public boolean checkStatus(PetStatus status) {
        if (!status.equals(FREE)) {
            log.info("СТАТУС {} НЕ СООТВЕТСТВУЕТ ДЛЯ УСЫНОВЛЕНИЯ", status);
            throw new PetStatusException();
        }
        log.info("СТАТУС {} СООТВЕТСТВУЕТ ДЛЯ УСЫНОВЛЕНИЯ", status);
        return true;
    }

    @Override
    public boolean checkTypes(PetType typeOne, PetType typeTwo) {
        if (!typeOne.equals(typeTwo)) {
            log.info("ТИПЫ {} И {} РАЗНЫЕ", typeOne, typeTwo);
            throw new DifferentTypesException();
        }
        log.info("ТИПЫ {} И {} ОДИНАКОВЫЕ", typeOne, typeTwo);
        return true;
    }

    @Override
    public String validatePhoneNumber(String phoneNumber) {
        String first = phoneNumber.substring(0, 1).replace(phoneNumber.charAt(0), '7');
        String second = phoneNumber.substring(1, 4);
        String third = phoneNumber.substring(4, 7);
        String fourth = phoneNumber.substring(7, 9);
        String fives = phoneNumber.substring(9);

        log.info("НОМЕР ТЕЛЕФОНА {} ОТФОРМАТИРОВАН", phoneNumber);
        return String.format("+%s-%s-%s-%s-%s", first, second, third, fourth, fives);
    }

    @Override
    public boolean checkPhoneNumber(String phoneNumber) {
        if (phoneNumber == null
                || phoneNumber.isBlank()
                || !phoneNumber.matches("[0-9]{11}")) {
            log.info("НОМЕР ТЕЛЕФОНА {} НЕКОРРЕКТЕН", phoneNumber);
            throw new InvalideNumberException();
        }
        log.info("НОМЕР ТЕЛЕФОНА {} КОРРЕКТЕН", phoneNumber);
        return true;
    }

    @Override
    public boolean checkName(String name) {
        if (name == null
                || name.isBlank()
                || !name.matches("[а-яА-Я -]+")) {
            log.info("ЗНАЧЕНИЕ {} НЕКОРРЕКТНО", name);
            throw new InvalideInputException();
        }
        log.info("ЗНАЧЕНИЕ {} КОРРЕКТНО", name);
        return true;
    }

    @Override
    public boolean checkAddress(String address) {
        if (address == null
                || address.isBlank()
                || !address.matches("[0-9а-яА-Я -,.]+")) {
            log.info("АДРЕС {} НЕКОРРЕКТЕН", address);
            throw new InvalideInputException();
        }
        log.info("АДРЕС {} КОРРЕКТЕН", address);
        return true;
    }

    @Override
    public boolean checkValue(Long value) {
        if (value == null || value <= 0) {
            log.info("ЗНАЧЕНИЕ {} НЕКОРРЕКТНО", value);
            throw new InvalideInputException();
        }
        log.info("ЗНАЧЕНИЕ {} КОРРЕКТНО", value);
        return true;
    }

    @Override
    public boolean checkParentAlreadyAdded(Collection<Parent> parents, Parent parent) {
        for (Parent element : parents) {
            if (element.getPhoneNumber().equals(parent.getPhoneNumber())) {
                log.info("УСЫНОВИТЕЛЬ УЖЕ БЫЛ ДОБАВЛЕН РАНЕЕ");
                throw new ParentAlreadyAddedException();
            }
        }
        log.info("УСЫНОВИТЕЛЬ НЕ БЫЛ ДОБАВЛЕН РАНЕЕ");
        return true;
    }

    @Override
    public boolean checkPetAlreadyAdded(Collection<Pet> pets, Pet pet) {
        for (Pet element : pets) {
            if (element.getName().equalsIgnoreCase(pet.getName())) {
                log.info("ЖИВОТНОЕ УЖЕ БЫЛО ДОБАВЛЕНО РАНЕЕ");
                throw new PetAlreadyAddedException();
            }
        }
        log.info("ЖИВОТНОЕ НЕ БЫЛО ДОБАВЛЕНО РАНЕЕ");
        return true;
    }

    @Override
    public boolean checkVolunteerAlreadyAdded(Collection<Volunteer> volunteers, Volunteer volunteer) {
        for (Volunteer element : volunteers) {
            if (element.getPhoneNumber().equals(volunteer.getPhoneNumber())) {
                log.info("ВОЛОНТЕР УЖЕ БЫЛ ДОБАВЛЕН РАНЕЕ");
                throw new VolunteerAlreadyAddedException();
            }
        }
        log.info("ВОЛОНТЕР НЕ БЫЛ ДОБАВЛЕН РАНЕЕ");
        return true;
    }

    @Override
    public boolean checkShelterAlreadyAdded(Collection<Shelter> shelters, Shelter shelter) {
        for (Shelter element : shelters) {
            if (element.getAddress().equalsIgnoreCase(shelter.getAddress())) {
                log.info("ПРИЮТ ЖЕ БЫЛ ДОБАВЛЕН РАНЕЕ");
                throw new ShelterAlreadyAddedException();
            }
        }
        log.info("ПРИЮТ НЕ БЫЛ ДОБАВЛЕН РАНЕЕ");
        return true;
    }
}
package com.skypro.ShelterPetTelegramBot.service.impl;

import com.skypro.ShelterPetTelegramBot.exception.DifferentTypesException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideNumberException;
import com.skypro.ShelterPetTelegramBot.exception.PetStatusException;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.FREE;

/**
 * Класс {@link CheckServiceImpl}
 * является сервисным классом для проверок
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Override
    public void checkFullName(String firstName,
                              String lastName) {

        checkName(firstName);
        checkName(lastName);
    }

    @Override
    public void checkPet(PetType typeOne,
                         PetType typeTwo,
                         String name,
                         Pet pet,
                         Collection<Pet> pets) {

        checkTypes(typeOne, typeTwo);
        checkName(name);
        checkPetAlreadyAdded(pets, pet);
    }

    @Override
    public void checkShelter(String address,
                             Shelter shelter,
                             Collection<Shelter> shelters) {

        checkAddress(address);
        checkShelterAlreadyAdded(shelters, shelter);
    }

    @Override
    public void checkStatus(PetStatus status) {
        if (!status.equals(FREE)) {
            throw new PetStatusException();
        }
    }

    @Override
    public void checkTypes(PetType typeOne, PetType typeTwo) {
        if (!typeOne.equals(typeTwo)) {
            throw new DifferentTypesException();
        }
    }

    @Override
    public String validatePhoneNumber(String phoneNumber) {
        checkPhoneNumber(phoneNumber);

        String first = phoneNumber.substring(0, 1).replace(phoneNumber.charAt(0), '7');
        String second = phoneNumber.substring(1, 4);
        String third = phoneNumber.substring(4, 7);
        String fourth = phoneNumber.substring(7, 9);
        String fives = phoneNumber.substring(9);

        return String.format("+%s-%s-%s-%s-%s", first, second, third, fourth, fives);
    }

    @Override
    public void checkPhoneNumber(String phoneNumber) {
        if (phoneNumber == null
                || phoneNumber.isBlank()
                || !phoneNumber.matches("[0-9]{11}")) {
            throw new InvalideNumberException();
        }
    }

    @Override
    public void checkName(String name) {
        if (name == null
                || name.isBlank()
                || !name.matches("[а-яА-Я -]+")) {
            throw new InvalideInputException();
        }
    }

    @Override
    public void checkAddress(String address) {
        if (address == null
                || address.isBlank()
                || !address.matches("[0-9а-яА-Я -,.]+")) {
            throw new InvalideInputException();
        }
    }

    @Override
    public void checkValue(Long value) {
        if (value == null || value <= 0) {
            throw new InvalideInputException();
        }
    }

    @Override
    public void checkParentAlreadyAdded(Collection<Parent> parents, String phoneNumber) {
        for (Parent element : parents) {
            if (element.getPhoneNumber().equals(phoneNumber)) {
                throw new ParentAlreadyAddedException();
            }
        }
    }

    @Override
    public void checkPetAlreadyAdded(Collection<Pet> pets, Pet pet) {
        for (Pet element : pets) {
            if (element.getName().equalsIgnoreCase(pet.getName())) {
                throw new PetAlreadyAddedException();
            }
        }
    }

    @Override
    public void checkVolunteerAlreadyAdded(Collection<Volunteer> volunteers, String phoneNumber) {
        for (Volunteer element : volunteers) {
            if (element.getPhoneNumber().equals(phoneNumber)) {
                throw new VolunteerAlreadyAddedException();
            }
        }
    }

    @Override
    public void checkShelterAlreadyAdded(Collection<Shelter> shelters, Shelter shelter) {
        for (Shelter element : shelters) {
            if (element.getAddress().equalsIgnoreCase(shelter.getAddress())) {
                throw new ShelterAlreadyAddedException();
            }
        }
    }
}
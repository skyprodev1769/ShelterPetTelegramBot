package com.skypro.ShelterPetTelegramBot.service.impl;

import com.skypro.ShelterPetTelegramBot.exception.DifferentSheltersException;
import com.skypro.ShelterPetTelegramBot.exception.DifferentTypesException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.number.InvalideNumberException;
import com.skypro.ShelterPetTelegramBot.exception.number.NumberAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link CheckServiceImpl}
 * является сервисным классом для проверок
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Override
    public void checkParent(String firstName,
                            String lastName,
                            String phoneNumber,
                            Shelter shelterOne,
                            Shelter shelterTwo,
                            Parent parent,
                            Collection<Parent> parents) {

        validateName(firstName);
        validateName(lastName);
        validatePhoneNumber(phoneNumber);
        validateShelters(shelterOne, shelterTwo);
        isParentAlreadyAdded(parents, parent);
    }

    @Override
    public void checkVolunteer(String firstName,
                               String lastName,
                               String phoneNumber,
                               Volunteer volunteer,
                               Collection<Volunteer> volunteers) {

        validateName(firstName);
        validateName(lastName);
        validatePhoneNumber(phoneNumber);
        isVolunteerAlreadyAdded(volunteers, volunteer);
    }

    @Override
    public void checkPet(PetType typeOne,
                         PetType typeTwo,
                         String name,
                         Pet pet,
                         Collection<Pet> pets) {

        validateTypes(typeOne, typeTwo);
        validateName(name);
        isPetAlreadyAdded(pets, pet);
    }

    @Override
    public void checkShelter(String address,
                             Shelter shelter,
                             Collection<Shelter> shelters) {

        validateAddress(address);
        isShelterAlreadyAdded(shelters, shelter);
    }

    @Override
    public void validateLong(Long value) {
        if (value == null || value <= 0) {
            throw new InvalideInputException();
        }
    }

    @Override
    public void validateName(String name) {
        if (name == null
                || name.isBlank()
                || !name.matches("[а-яА-Я -]+")) {
            throw new InvalideInputException();
        }
    }

    @Override
    public void validateAddress(String address) {
        if (address == null
                || address.isBlank()
                || !address.matches("[0-9а-яА-Я -,.]+")) {
            throw new InvalideInputException();
        }
    }

    @Override
    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null
                || phoneNumber.isBlank()
                || !phoneNumber.matches("[0-9]{11}")) {
            throw new InvalideNumberException();
        }
    }

    @Override
    public void validateShelters(Shelter shelterOne, Shelter shelterTwo) {
        if (!shelterOne.equals(shelterTwo)) {
            throw new DifferentSheltersException();
        }
    }

    @Override
    public void validateTypes(PetType typeOne, PetType typeTwo) {
        if (!typeOne.equals(typeTwo)) {
            throw new DifferentTypesException();
        }
    }

    @Override
    public void isPhoneNumberParentAlreadyAdded(Collection<Parent> parents, String phoneNumber) {
        for (Parent element : parents) {
            if (element.getPhoneNumber().equals(phoneNumber)) {
                throw new NumberAlreadyAddedException();
            }
        }
    }

    @Override
    public void isPhoneNumberVolunteerAlreadyAdded(Collection<Volunteer> volunteers, String phoneNumber) {
        for (Volunteer element : volunteers) {
            if (element.getPhoneNumber().equals(phoneNumber)) {
                throw new NumberAlreadyAddedException();
            }
        }
    }

    @Override
    public void isShelterAlreadyAdded(Collection<Shelter> shelters, Shelter shelter) {
        for (Shelter element : shelters) {

            if (element.getType().equals(shelter.getType())
                    && element.getAddress().equalsIgnoreCase(shelter.getAddress())) {

                throw new ShelterAlreadyAddedException();
            }
        }
    }

    @Override
    public void isVolunteerAlreadyAdded(Collection<Volunteer> volunteers, Volunteer volunteer) {
        for (Volunteer element : volunteers) {

            if (element.getFirstName().equalsIgnoreCase(volunteer.getFirstName())
                    && element.getLastName().equalsIgnoreCase(volunteer.getLastName())
                    && element.getPhoneNumber().equals(volunteer.getPhoneNumber())
                    && element.getShelter().equals(volunteer.getShelter())) {

                throw new VolunteerAlreadyAddedException();
            }
        }
    }

    @Override
    public void isPetAlreadyAdded(Collection<Pet> pets, Pet pet) {
        for (Pet element : pets) {

            if (element.getName().equalsIgnoreCase(pet.getName())
                    && element.getType().equals(pet.getType())
                    && element.getShelter().equals(pet.getShelter())) {

                throw new PetAlreadyAddedException();
            }
        }
    }

    @Override
    public void isParentAlreadyAdded(Collection<Parent> parents, Parent parent) {
        for (Parent element : parents) {

            if (element.getFirstName().equalsIgnoreCase(parent.getFirstName())
                    && element.getLastName().equalsIgnoreCase(parent.getLastName())
                    && element.getPhoneNumber().equals(parent.getPhoneNumber())
                    && element.getVolunteer().equals(parent.getVolunteer())
                    && element.getPet().equals(parent.getPet())) {

                throw new ParentAlreadyAddedException();
            }
        }
    }
}
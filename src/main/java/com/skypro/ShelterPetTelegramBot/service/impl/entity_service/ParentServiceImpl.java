package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ParentService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link ParentServiceImpl}
 * является сервисным классом для контроллера {@link ParentController}
 */
@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository repository;
    private final CheckService checkService;
    private final VolunteerService volunteerService;
    private final PetService petService;

    public ParentServiceImpl(ParentRepository repository, CheckService checkService, VolunteerService volunteerService, PetService petService) {
        this.repository = repository;
        this.checkService = checkService;
        this.volunteerService = volunteerService;
        this.petService = petService;
    }

    @Override
    public Parent add(String firstName,
                      String lastName,
                      String phoneNumber,
                      Long volunteerId,
                      Long petId) {

        Volunteer volunteer = volunteerService.getById(volunteerId);
        Pet pet = petService.getById(petId);
        Parent parent = new Parent(firstName, lastName, phoneNumber, volunteer, pet);

        Shelter shelterOne = volunteer.getShelter();
        Shelter shelterTwo = pet.getShelter();

        checkService.checkParent(firstName, lastName, phoneNumber, shelterOne, shelterTwo, parent, getAll());
        checkService.isPhoneNumberParentAlreadyAdded(getAll(), phoneNumber);
        checkService.isPhoneNumberVolunteerAlreadyAdded(volunteerService.getAll(), phoneNumber);

        volunteer.setPhoneNumber(checkService.changePhoneNumber(phoneNumber));

        return repository.save(parent);
    }

    @Override
    public Parent getById(Long id) {
        checkService.validateLong(id);
        return repository.findById(id).orElseThrow(ParentNotFoundException::new);
    }

    @Override
    public Collection<Parent> getAllByParameters(String firstName, String lastName, String phoneNumber, Long volunteerId) {

        if (firstName != null) {
            checkService.validateName(firstName);
            return repository.getAllByFirstNameContainsIgnoreCase(firstName);

        } else if (lastName != null) {
            checkService.validateName(lastName);
            return repository.getAllByLastNameContainsIgnoreCase(lastName);

        } else if (phoneNumber != null) {
            return repository.getAllByPhoneNumberContains(phoneNumber);

        } else if (volunteerId != null) {
            checkService.validateLong(volunteerId);
            return repository.getAllByVolunteerId(volunteerId);

        } else {
            return getAll();
        }
    }

    @Override
    public Collection<Parent> getAll() {
        return repository.findAll();
    }

    @Override
    public Parent edit(Long id,
                       String firstName,
                       String lastName,
                       String phoneNumber,
                       Long volunteerId,
                       Long petId) {

        Parent parent = getById(id);

        if (firstName == null & lastName == null & phoneNumber == null & volunteerId == null & petId == null) {
            return parent;

        } else {

            Parent edit = new Parent(parent.getFirstName(), parent.getLastName(), parent.getPhoneNumber(), parent.getVolunteer(), parent.getPet());

            if (firstName != null) {
                edit.setFirstName(firstName);
            }

            if (lastName != null) {
                edit.setLastName(lastName);
            }

            if (phoneNumber != null) {
                checkService.isPhoneNumberParentAlreadyAdded(getAll(), phoneNumber);
                checkService.isPhoneNumberVolunteerAlreadyAdded(volunteerService.getAll(), phoneNumber);
                edit.setPhoneNumber(phoneNumber);
            }

            if (volunteerId != null) {
                Volunteer volunteer = volunteerService.getById(volunteerId);
                edit.setVolunteer(volunteer);
            }

            if (petId != null) {
                Pet pet = petService.getById(petId);
                edit.setPet(pet);
            }

            Shelter shelterOne = edit.getVolunteer().getShelter();
            Shelter shelterTwo = edit.getPet().getShelter();

            edit.setId(parent.getId());
            checkService.checkParent(edit.getFirstName(), edit.getLastName(), edit.getPhoneNumber(), shelterOne, shelterTwo, edit, getAll());
            edit.setPhoneNumber(checkService.changePhoneNumber(phoneNumber));
            return repository.save(edit);
        }
    }

    @Override
    public Parent delete(Long id) {
        Parent parent = getById(id);
        repository.delete(parent);
        return parent;
    }
}
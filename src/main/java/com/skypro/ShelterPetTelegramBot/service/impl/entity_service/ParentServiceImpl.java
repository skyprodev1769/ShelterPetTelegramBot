package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ParentService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link ParentServiceImpl}
 * является сервисным классом для контроллера {@link ParentController}
 */
@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    ParentRepository repository;
    @Autowired
    CheckService checkService;
    @Autowired
    VolunteerService volunteerService;
    @Autowired
    PetService petService;

    @Override
    public Parent add(String firstName,
                      String lastName,
                      String phoneNumber,
                      Long volunteerId,
                      Long petId) {

        Volunteer volunteer = volunteerService.get(volunteerId);
        Pet pet = petService.get(petId);
        Parent parent = new Parent(firstName, lastName, phoneNumber, volunteer, pet);

        PetType typeOne = volunteer.getShelter().getType();
        PetType typeTwo = pet.getShelter().getType();

        checkService.checkParent(firstName, lastName, phoneNumber, typeOne, typeTwo, parent, getAll());
        checkService.isPhoneNumberParentAlreadyAdded(getAll(), phoneNumber);
        checkService.isPhoneNumberVolunteerAlreadyAdded(volunteerService.getAll(), phoneNumber);

        return repository.save(parent);
    }

    @Override
    public Parent get(Long id) {
        checkService.validateLong(id);
        return repository.findById(id).orElseThrow(ParentNotFoundException::new);
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

        Parent parent = get(id);

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
                Volunteer volunteer = volunteerService.get(volunteerId);
                edit.setVolunteer(volunteer);
            }

            if (petId != null) {
                Pet pet = petService.get(petId);
                edit.setPet(pet);
            }

            PetType typeOne = edit.getVolunteer().getShelter().getType();
            PetType typeTwo = edit.getPet().getShelter().getType();

            edit.setId(parent.getId());
            checkService.checkParent(edit.getFirstName(), edit.getLastName(), edit.getPhoneNumber(), typeOne, typeTwo, edit, getAll());
            return repository.save(edit);
        }
    }

    @Override
    public Parent delete(Long id) {
        Parent parent = get(id);
        repository.delete(parent);
        return parent;
    }
}
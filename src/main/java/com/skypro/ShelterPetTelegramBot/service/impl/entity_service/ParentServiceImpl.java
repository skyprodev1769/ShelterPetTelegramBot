package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
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
    VolunteerService volunteerService;
    @Autowired
    PetService petService;
    @Autowired
    ParentRepository repository;

    @Override
    public Parent add(String firstName,
                      String lastName,
                      String phoneNumber,
                      Long volunteerId,
                      Long petId) {

        Volunteer volunteer = volunteerService.get(volunteerId);
        Pet pet = petService.get(petId);
        Parent parent = new Parent(firstName, lastName, phoneNumber, volunteer, pet);
        return repository.save(parent);
    }

    @Override
    public Parent get(Long id) {
        return repository.findById(id).orElseThrow();
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
        Volunteer volunteer = volunteerService.get(volunteerId);
        Pet pet = petService.get(petId);

        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setPhoneNumber(phoneNumber);
        parent.setVolunteer(volunteer);
        parent.setPet(pet);

        return repository.save(parent);
    }

    @Override
    public Parent delete(Long id) {
        Parent parent = get(id);
        repository.delete(parent);
        return parent;
    }
}
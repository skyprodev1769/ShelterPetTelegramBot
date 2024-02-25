package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.VolunteerController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.VolunteerRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link VolunteerServiceImpl}
 * является сервисным классом для контроллера {@link VolunteerController}
 */
@Service
public class VolunteerServiceImpl implements VolunteerService {

    @Autowired
    VolunteerRepository repository;
    @Autowired
    ShelterService shelterService;

    @Override
    public Volunteer add(String firstName,
                         String lastName,
                         String phoneNumber,
                         Long shelterId) {

        Shelter shelter = shelterService.get(shelterId);
        Volunteer volunteer = new Volunteer(firstName, lastName, phoneNumber, shelter);
        return repository.save(volunteer);
    }

    @Override
    public Volunteer get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Collection<Volunteer> getAll() {
        return repository.findAll();
    }

    @Override
    public Volunteer edit(Long id,
                          String firstName,
                          String lastName,
                          String phoneNumber,
                          Long shelterId) {

        Volunteer volunteer = get(id);
        Shelter shelter = shelterService.get(shelterId);

        volunteer.setFirstName(firstName);
        volunteer.setLastName(lastName);
        volunteer.setPhoneNumber(phoneNumber);
        volunteer.setShelter(shelter);

        return repository.save(volunteer);
    }

    @Override
    public Volunteer delete(Long id) {
        Volunteer volunteer = get(id);
        repository.delete(volunteer);
        return volunteer;
    }
}

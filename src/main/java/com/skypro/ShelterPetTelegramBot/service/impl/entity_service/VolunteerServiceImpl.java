package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.VolunteerController;
import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.VolunteerRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
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
    CheckService checkService;
    @Autowired
    ShelterService shelterService;

    @Override
    public Volunteer add(String firstName,
                         String lastName,
                         String phoneNumber,
                         Long shelterId) {

        Shelter shelter = shelterService.get(shelterId);
        Volunteer volunteer = new Volunteer(firstName, lastName, phoneNumber, shelter);

        checkService.checkVolunteer(firstName, lastName, phoneNumber, volunteer, getAll());
        checkService.isPhoneNumberVolunteerAlreadyAdded(getAll(), phoneNumber);

        return repository.save(volunteer);
    }

    @Override
    public Volunteer getById(Long id) {
        checkService.validateLong(id);
        return repository.findById(id).orElseThrow(VolunteerNotFoundException::new);
    }

    @Override
    public Collection<Volunteer> getAllByParameters(String firstName,
                                                    String lastName,
                                                    String phoneNumber,
                                                    Long shelterId) {

        if (firstName != null) {
            checkService.validateName(firstName);
            return repository.getAllByFirstNameContainsIgnoreCase(firstName);

        } else if (lastName != null) {
            checkService.validateName(lastName);
            return repository.getAllByLastNameContainsIgnoreCase(lastName);

        } else if (phoneNumber != null) {
            checkService.validatePhoneNumber(phoneNumber);
            return repository.getAllByPhoneNumberContains(phoneNumber);

        } else if (shelterId != null) {
            checkService.validateLong(shelterId);
            return repository.getAllByShelterId(shelterId);

        } else {
            return getAll();
        }
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

        Volunteer volunteer = getById(id);

        if (firstName == null & lastName == null & phoneNumber == null & shelterId == null) {
            return volunteer;

        } else {

            Volunteer edit = new Volunteer(volunteer.getFirstName(), volunteer.getLastName(), volunteer.getPhoneNumber(), volunteer.getShelter());

            if (firstName != null) {
                edit.setFirstName(firstName);
            }

            if (lastName != null) {
                edit.setLastName(lastName);
            }

            if (phoneNumber != null) {
                checkService.isPhoneNumberVolunteerAlreadyAdded(getAll(), phoneNumber);
                edit.setPhoneNumber(phoneNumber);
            }

            if (shelterId != null) {
                Shelter shelter = shelterService.get(shelterId);
                edit.setShelter(shelter);
            }

            edit.setId(volunteer.getId());
            checkService.checkVolunteer(edit.getFirstName(), edit.getLastName(), edit.getPhoneNumber(), edit, getAll());
            return repository.save(edit);
        }
    }

    @Override
    public Volunteer delete(Long id) {
        Volunteer volunteer = getById(id);
        repository.delete(volunteer);
        return volunteer;
    }
}
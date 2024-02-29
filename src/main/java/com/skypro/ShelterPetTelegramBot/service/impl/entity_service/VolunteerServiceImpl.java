package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.VolunteerController;
import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.VolunteerRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link VolunteerServiceImpl}
 * является сервисным классом для контроллера {@link VolunteerController}
 */
@Slf4j
@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository repository;
    private final CheckService checkService;
    private final ShelterService shelterService;

    public VolunteerServiceImpl(VolunteerRepository repository,
                                CheckService checkService,
                                ShelterService shelterService) {

        this.repository = repository;
        this.checkService = checkService;
        this.shelterService = shelterService;
    }

    @Override
    public Volunteer add(String firstName,
                         String lastName,
                         String phoneNumber,
                         Long shelterId) {

        Shelter shelter = shelterService.getById(shelterId);
        Volunteer volunteer = new Volunteer(firstName, lastName, phoneNumber, shelter);
        checkService.checkFullName(firstName, lastName);

        phoneNumber = checkService.validatePhoneNumber(phoneNumber);
        checkService.checkPhoneNumberVolunteerAlreadyAdded(getAll(), phoneNumber);
        volunteer.setPhoneNumber(phoneNumber);

        log.info("ДОБАВЛЕН НОВЫЙ ВОЛОНТЕР {} {} {} {}", firstName, lastName, phoneNumber, shelterId);
        return repository.save(volunteer);
    }

    @Override
    public Volunteer getById(Long id) {
        checkService.checkValue(id);
        log.info("ПОЛУЧЕН ВОЛОНТЕР {}", id);
        return repository.findById(id).orElseThrow(VolunteerNotFoundException::new);
    }

    @Override
    public Collection<Volunteer> getAllByParameters(String firstName,
                                                    String lastName,
                                                    String phoneNumber,
                                                    Long shelterId) {

        if (firstName != null) {
            checkService.checkName(firstName);
            log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ {}", firstName);
            return repository.getAllByFirstNameContainsIgnoreCase(firstName);

        } else if (lastName != null) {
            checkService.checkName(lastName);
            log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ {}", lastName);
            return repository.getAllByLastNameContainsIgnoreCase(lastName);

        } else if (phoneNumber != null) {
            log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО НОМЕРУ ТЕЛЕФОНА {}", phoneNumber);
            return repository.getAllByPhoneNumberContains(phoneNumber);

        } else if (shelterId != null) {
            checkService.checkValue(shelterId);
            log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО id ПРИЮТА ДЛЯ ЖИВОТНЫХ {}", shelterId);
            return repository.getAllByShelterId(shelterId);

        } else {
            return getAll();
        }
    }

    @Override
    public Collection<Volunteer> getAll() {
        log.info("ПОЛУЧЕНЫ ВСЕ ВОЛОНТЕРЫ");
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
            edit.setId(volunteer.getId());

            if (firstName != null) {
                checkService.checkName(firstName);
                edit.setFirstName(firstName);
            }

            if (lastName != null) {
                checkService.checkName(lastName);
                edit.setLastName(lastName);
            }

            if (phoneNumber != null) {
                phoneNumber = checkService.validatePhoneNumber(phoneNumber);
                checkService.checkPhoneNumberVolunteerAlreadyAdded(getAll(), phoneNumber);
                edit.setPhoneNumber(phoneNumber);
            }

            if (shelterId != null) {
                Shelter shelter = shelterService.getById(shelterId);
                edit.setShelter(shelter);
            }

            log.info("ИЗМЕНЕНЫ ДАННЫЕ ВОЛОНТЕРА {}", id);
            return repository.save(edit);
        }
    }

    @Override
    public Volunteer delete(Long id) {
        Volunteer volunteer = getById(id);
        repository.delete(volunteer);

        log.info("УДАЛЕН ВОЛОНТЕР {}", id);
        return volunteer;
    }
}
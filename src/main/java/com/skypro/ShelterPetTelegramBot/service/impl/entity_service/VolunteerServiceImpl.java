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
        checkService.checkVolunteerAlreadyAdded(getAll(), phoneNumber);
        volunteer.setPhoneNumber(phoneNumber);

        log.info("ДОБАВЛЕН НОВЫЙ ВОЛОНТЕР: \"имя\" - {}; \"фамилия\" - {}; \"номер телефона\" - {}; \"ID приюта для животных\" - {}", firstName, lastName, phoneNumber, shelterId);
        return repository.save(volunteer);
    }

    @Override
    public Volunteer getById(Long id) {
        checkService.checkValue(id);
        log.info("ПОЛУЧЕН ВОЛОНТЕР ПО ID - {}", id);
        return repository.findById(id).orElseThrow(VolunteerNotFoundException::new);
    }

    @Override
    public Collection<Volunteer> getAllByParameters(String firstName,
                                                    String lastName,
                                                    String phoneNumber,
                                                    Long shelterId) {

        if (firstName == null & lastName == null & phoneNumber == null & shelterId == null) {
            return getAll();

        } else {

            if (firstName != null & lastName == null & phoneNumber == null & shelterId == null) {
                checkService.checkName(firstName);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}", firstName);
                return repository.getAllByFirstNameContainsIgnoreCase(firstName);

            } else if (firstName == null & lastName != null & phoneNumber == null & shelterId == null) {
                checkService.checkName(lastName);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {}", lastName);
                return repository.getAllByLastNameContainsIgnoreCase(lastName);

            } else if (firstName == null & lastName == null & phoneNumber != null & shelterId == null) {
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО НОМЕРУ ТЕЛЕФОНА - {}", phoneNumber);
                return repository.getAllByPhoneNumberContains(phoneNumber);

            } else if (firstName == null & lastName == null & phoneNumber == null & shelterId != null) {
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", shelterId);
                return repository.getAllByShelterId(shelterId);

            } else if (firstName != null & lastName != null & phoneNumber == null & shelterId == null) {
                checkService.checkFullName(firstName, lastName);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {} И ФАМИЛИИ - {}", firstName, lastName);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(firstName, lastName);

            } else if (firstName != null & lastName == null & phoneNumber != null & shelterId == null) {
                checkService.checkName(firstName);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", firstName, phoneNumber);
                return repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(firstName, phoneNumber);

            } else if (firstName != null & lastName == null & phoneNumber == null & shelterId != null) {
                checkService.checkName(firstName);
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndShelterId(firstName, shelterId);

            } else if (firstName == null & lastName != null & phoneNumber != null & shelterId == null) {
                checkService.checkName(lastName);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", lastName, phoneNumber);
                return repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(lastName, phoneNumber);

            } else if (firstName == null & lastName != null & phoneNumber == null & shelterId != null) {
                checkService.checkName(lastName);
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", lastName, shelterId);
                return repository.getAllByLastNameContainsIgnoreCaseAndShelterId(lastName, shelterId);

            } else if (firstName == null & lastName == null & phoneNumber != null & shelterId != null) {
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", phoneNumber, shelterId);
                return repository.getAllByPhoneNumberContainsAndShelterId(phoneNumber, shelterId);

            } else if (firstName != null & lastName != null & phoneNumber != null & shelterId == null) {
                checkService.checkFullName(firstName, lastName);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, ФАМИЛИИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", firstName, lastName, phoneNumber);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumber(firstName, lastName, phoneNumber);

            } else if (firstName != null & lastName != null & phoneNumber == null) {
                checkService.checkFullName(firstName, lastName);
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, ФАМИЛИИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, lastName, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(firstName, lastName, shelterId);

            } else if (firstName != null & lastName == null) {
                checkService.checkName(firstName);
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, phoneNumber, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(firstName, phoneNumber, shelterId);

            } else if (firstName == null) {
                checkService.checkName(lastName);
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {}, НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", lastName, phoneNumber, shelterId);
                return repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(lastName, phoneNumber, shelterId);

            } else {
                checkService.checkFullName(firstName, lastName);
                checkService.checkValue(shelterId);
                log.info("ПОЛУЧЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, ФАМИЛИИ - {}, НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, lastName, phoneNumber, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(firstName, lastName, phoneNumber, shelterId);
            }
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
                log.info("ИЗМЕНЕНО ИМЯ ВОЛОНТЕРА ПО ID - {} НА \"имя\" - {}", id, firstName);
            }

            if (lastName != null) {
                checkService.checkName(lastName);
                edit.setLastName(lastName);
                log.info("ИЗМЕНЕНА ФАМИЛИЯ ВОЛОНТЕРА ПО ID - {} НА \"фамилию\" - {}", id, lastName);
            }

            if (phoneNumber != null) {
                phoneNumber = checkService.validatePhoneNumber(phoneNumber);
                checkService.checkVolunteerAlreadyAdded(getAll(), phoneNumber);
                edit.setPhoneNumber(phoneNumber);
                log.info("ИЗМЕНЕН НОМЕР ТЕЛЕФОНА ВОЛОНТЕРА ПО ID - {} НА \"номер телефона\" - {}", id, phoneNumber);
            }

            if (shelterId != null) {
                Shelter shelter = shelterService.getById(shelterId);
                edit.setShelter(shelter);
                log.info("ИЗМЕНЕН ПРИЮТ ВОЛОНТЕРА ПО ID - {} НА \"ID приюта для животных\" - {}", id, shelterId);
            }

            return repository.save(edit);
        }
    }

    @Override
    public Volunteer delete(Long id) {
        Volunteer volunteer = getById(id);
        repository.delete(volunteer);
        log.info("УДАЛЕН ВОЛОНТЕР ПО ID - {}", id);
        return volunteer;
    }
}
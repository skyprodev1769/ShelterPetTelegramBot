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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_ID;
import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_VOLUNTEER;

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

        checkService.checkName(firstName);
        checkService.checkName(lastName);
        checkService.checkPhoneNumber(phoneNumber);

        Shelter shelter = shelterService.getById(shelterId);
        phoneNumber = checkService.validatePhoneNumber(phoneNumber);
        Volunteer volunteer = new Volunteer(firstName, lastName, phoneNumber, shelter);

        checkService.checkVolunteerAlreadyAdded(getAll(), volunteer);

        log.info("СОЗДАН НОВЫЙ ВОЛОНТЕР: \"имя\" - {}; \"фамилия\" - {}; \"номер телефона\" - {}; \"ID приюта для животных\" - {}", firstName, lastName, phoneNumber, shelterId);
        return repository.save(volunteer);
    }

    @Override
    @Cacheable(value = CACHE_VOLUNTEER)
    public Volunteer getById(Long id) {
        checkService.checkValue(id);
        log.info("ЗАПРОШЕН ВОЛОНТЕР ПО ID - {}", id);
        return repository.findById(id).orElseThrow(VolunteerNotFoundException::new);
    }

    @Override
    @Cacheable(value = CACHE_VOLUNTEER)
    public Collection<Volunteer> getAllByParameters(String firstName,
                                                    String lastName,
                                                    String phoneNumber,
                                                    Long shelterId) {

        if (firstName == null & lastName == null & phoneNumber == null & shelterId == null) {
            return getAll();

        } else {

            if (firstName != null & lastName == null & phoneNumber == null & shelterId == null) {
                checkService.checkName(firstName);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}", firstName);
                return repository.getAllByFirstNameContainsIgnoreCase(firstName);

            } else if (firstName == null & lastName != null & phoneNumber == null & shelterId == null) {
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {}", lastName);
                return repository.getAllByLastNameContainsIgnoreCase(lastName);

            } else if (firstName == null & lastName == null & phoneNumber != null & shelterId == null) {
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО НОМЕРУ ТЕЛЕФОНА - {}", phoneNumber);
                return repository.getAllByPhoneNumberContains(phoneNumber);

            } else if (firstName == null & lastName == null & phoneNumber == null & shelterId != null) {
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", shelterId);
                return repository.getAllByShelterId(shelterId);

            } else if (firstName != null & lastName != null & phoneNumber == null & shelterId == null) {
                checkService.checkName(firstName);
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {} И ФАМИЛИИ - {}", firstName, lastName);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(firstName, lastName);

            } else if (firstName != null & lastName == null & phoneNumber != null & shelterId == null) {
                checkService.checkName(firstName);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", firstName, phoneNumber);
                return repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(firstName, phoneNumber);

            } else if (firstName != null & lastName == null & phoneNumber == null & shelterId != null) {
                checkService.checkName(firstName);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndShelterId(firstName, shelterId);

            } else if (firstName == null & lastName != null & phoneNumber != null & shelterId == null) {
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", lastName, phoneNumber);
                return repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(lastName, phoneNumber);

            } else if (firstName == null & lastName != null & phoneNumber == null & shelterId != null) {
                checkService.checkName(lastName);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", lastName, shelterId);
                return repository.getAllByLastNameContainsIgnoreCaseAndShelterId(lastName, shelterId);

            } else if (firstName == null & lastName == null & phoneNumber != null & shelterId != null) {
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", phoneNumber, shelterId);
                return repository.getAllByPhoneNumberContainsAndShelterId(phoneNumber, shelterId);

            } else if (firstName != null & lastName != null & phoneNumber != null & shelterId == null) {
                checkService.checkName(firstName);
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, ФАМИЛИИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", firstName, lastName, phoneNumber);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(firstName, lastName, phoneNumber);

            } else if (firstName != null & lastName != null & phoneNumber == null) {
                checkService.checkName(firstName);
                checkService.checkName(lastName);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, ФАМИЛИИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, lastName, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(firstName, lastName, shelterId);

            } else if (firstName != null & lastName == null) {
                checkService.checkName(firstName);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, phoneNumber, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(firstName, phoneNumber, shelterId);

            } else if (firstName == null) {
                checkService.checkName(lastName);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ФАМИЛИИ - {}, НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", lastName, phoneNumber, shelterId);
                return repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(lastName, phoneNumber, shelterId);

            } else {
                checkService.checkName(firstName);
                checkService.checkName(lastName);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ВОЛОНТЕРЫ ПО ИМЕНИ - {}, ФАМИЛИИ - {}, НОМЕРУ ТЕЛЕФОНА - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", firstName, lastName, phoneNumber, shelterId);
                return repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(firstName, lastName, phoneNumber, shelterId);
            }
        }
    }

    @Override
    @Cacheable(value = CACHE_VOLUNTEER)
    public Collection<Volunteer> getAll() {
        log.info("ЗАПРОШЕНЫ ВСЕ ВОЛОНТЕРЫ");
        return repository.findAll();
    }

    @Override
    @CachePut(value = CACHE_VOLUNTEER, key = CACHE_ID)
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
                checkService.checkPhoneNumber(phoneNumber);
                phoneNumber = checkService.validatePhoneNumber(phoneNumber);
                edit.setPhoneNumber(phoneNumber);
                checkService.checkVolunteerAlreadyAdded(getAll(), edit);
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
    @CacheEvict(CACHE_VOLUNTEER)
    public Volunteer delete(Long id) {
        Volunteer volunteer = getById(id);
        repository.delete(volunteer);
        log.info("УДАЛЕН ВОЛОНТЕР ПО ID - {}", id);
        return volunteer;
    }
}
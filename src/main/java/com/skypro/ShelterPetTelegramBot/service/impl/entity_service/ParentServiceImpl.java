package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentNotFoundException;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ParentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.ADOPTED;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_ID;
import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_PARENT;

/**
 * Класс {@link ParentServiceImpl}
 * является сервисным классом для контроллера {@link ParentController}
 */
@Slf4j
@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final PetRepository petRepository;
    private final CheckService checkService;

    public ParentServiceImpl(ParentRepository parentRepository,
                             PetRepository petRepository,
                             CheckService checkService) {

        this.parentRepository = parentRepository;
        this.petRepository = petRepository;
        this.checkService = checkService;
    }

    @Override
    public Parent add(String firstName,
                      String lastName,
                      String phoneNumber,
                      String petName) {

        checkService.checkName(firstName);
        checkService.checkName(lastName);
        checkService.checkName(petName);
        checkService.checkPhoneNumber(phoneNumber);
        phoneNumber = checkService.validatePhoneNumber(phoneNumber);

        Pet pet = petRepository.getByNameContainsIgnoreCase(petName).orElseThrow(PetNotFoundException::new);
        checkService.checkStatus(pet.getStatus());
        pet.setStatus(ADOPTED);

        Parent parent = new Parent(firstName, lastName, phoneNumber, pet);

        checkService.checkParentAlreadyAdded(getAll(), parent);

        log.info("СОЗДАН НОВЫЙ УСЫНОВИТЕЛЬ: \"имя\" - {}; \"фамилия\" - {}; \"номер телефона\" - {}; \"имя животного\" - {}", firstName, lastName, phoneNumber, petName);
        return parentRepository.save(parent);
    }

    @Override
    @Cacheable(value = CACHE_PARENT)
    public Parent getById(Long id) {
        checkService.checkValue(id);
        log.info("ЗАПРОШЕН УСЫНОВИТЕЛЬ ПО ID - {}", id);
        return parentRepository.findById(id).orElseThrow(ParentNotFoundException::new);
    }

    @Override
    @Cacheable(value = CACHE_PARENT)
    public Collection<Parent> getAllByParameters(String firstName, String lastName, String phoneNumber) {

        if (firstName == null & lastName == null & phoneNumber == null) {
            return getAll();

        } else {

            if (firstName != null & lastName == null & phoneNumber == null) {
                checkService.checkName(firstName);
                log.info("ЗАПРОШЕНЫ УСЫНОВИТЕЛИ ПО ИМЕНИ - {}", firstName);
                return parentRepository.getAllByFirstNameContainsIgnoreCase(firstName);

            } else if (firstName == null & lastName != null & phoneNumber == null) {
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ УСЫНОВИТЕЛИ ПО ФАМИЛИИ - {}", lastName);
                return parentRepository.getAllByLastNameContainsIgnoreCase(lastName);

            } else if (firstName == null & lastName == null & phoneNumber != null) {
                log.info("ЗАПРОШЕНЫ УСЫНОВИТЕЛИ ПО НОМЕРУ ТЕЛЕФОНА - {}", phoneNumber);
                return parentRepository.getAllByPhoneNumberContains(phoneNumber);

            } else if (firstName != null & lastName != null & phoneNumber == null) {
                checkService.checkName(firstName);
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ УСЫНОВИТЕЛИ ПО ИМЕНИ - {} И ФАМИЛИИ - {}", firstName, lastName);
                return parentRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(firstName, lastName);

            } else if (firstName != null & lastName == null) {
                checkService.checkName(firstName);
                log.info("ЗАПРОШЕНЫ УСЫНОВИТЕЛИ ПО ИМЕНИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", firstName, phoneNumber);
                return parentRepository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(firstName, phoneNumber);

            } else if (firstName == null) {
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ УСЫНОВИТЕЛИ ПО ФАМИЛИИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", lastName, phoneNumber);
                return parentRepository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(lastName, phoneNumber);

            } else {
                checkService.checkName(firstName);
                checkService.checkName(lastName);
                log.info("ЗАПРОШЕНЫ УСЫНОВИТЕЛИ ПО ИМЕНИ - {}, ФАМИЛИИ - {} И НОМЕРУ ТЕЛЕФОНА - {}", firstName, lastName, phoneNumber);
                return parentRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(firstName, lastName, phoneNumber);
            }
        }
    }

    @Override
    @Cacheable(value = CACHE_PARENT)
    public Collection<Parent> getAll() {
        log.info("ЗАПРОШЕНЫ ВСЕ УСЫНОВИТЕЛИ");
        return parentRepository.findAll();
    }

    @Override
    @CachePut(value = CACHE_PARENT, key = CACHE_ID)
    public Parent edit(Long id,
                       String firstName,
                       String lastName,
                       String phoneNumber,
                       String petName) {

        Parent parent = getById(id);

        if (firstName == null & lastName == null & phoneNumber == null & petName == null) {
            return parent;

        } else {

            Parent edit = new Parent(parent.getFirstName(), parent.getLastName(), parent.getPhoneNumber(), parent.getPet());
            edit.setId(parent.getId());

            if (firstName != null) {
                checkService.checkName(firstName);
                edit.setFirstName(firstName);
                log.info("ИЗМЕНЕНО ИМЯ УСЫНОВИТЕЛЯ ПО ID - {} НА \"имя\" - {}", id, firstName);
            }

            if (lastName != null) {
                checkService.checkName(lastName);
                edit.setLastName(lastName);
                log.info("ИЗМЕНЕНА ФАМИЛИЯ УСЫНОВИТЕЛЯ ПО ID - {} НА \"фамилию\" - {}", id, lastName);
            }

            if (phoneNumber != null) {
                checkService.checkPhoneNumber(phoneNumber);
                phoneNumber = checkService.validatePhoneNumber(phoneNumber);
                edit.setPhoneNumber(phoneNumber);
                checkService.checkParentAlreadyAdded(getAll(), edit);
                log.info("ИЗМЕНЕН НОМЕР ТЕЛЕФОНА УСЫНОВИТЕЛЯ ПО ID - {} НА \"номер телефона\" - {}", id, phoneNumber);
            }

            if (petName != null) {
                checkService.checkName(petName);
                Pet pet = petRepository.getByNameContainsIgnoreCase(petName).orElseThrow(PetNotFoundException::new);
                checkService.checkStatus(pet.getStatus());
                pet.setStatus(ADOPTED);
                edit.getPet().setStatus(FREE);
                edit.setPet(pet);
                log.info("ИЗМЕНЕНО ЖИВОТНОЕ УСЫНОВИТЕЛЯ ПО ID - {} НА \"имя животного\" - {}", id, petName);
            }

            return parentRepository.save(edit);
        }
    }

    @Override
    @CacheEvict(CACHE_PARENT)
    public Parent delete(Long id) {
        Parent parent = getById(id);
        parentRepository.delete(parent);
        log.info("УДАЛЕН УСЫНОВИТЕЛЬ ПО ID - {}", id);
        return parent;
    }
}
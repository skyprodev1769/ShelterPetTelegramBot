package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ParentService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.*;

/**
 * Класс {@link ParentServiceImpl}
 * является сервисным классом для контроллера {@link ParentController}
 */
@Slf4j
@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository repository;
    private final CheckService checkService;
    private final PetService petService;

    public ParentServiceImpl(ParentRepository repository,
                             CheckService checkService,
                             PetService petService) {

        this.repository = repository;
        this.checkService = checkService;
        this.petService = petService;
    }

    @Override
    public Parent add(String firstName,
                      String lastName,
                      String phoneNumber,
                      Long petId) {

        Pet pet = petService.getById(petId);
        Parent parent = new Parent(firstName, lastName, phoneNumber, pet);
        checkService.checkFullName(firstName, lastName);

        phoneNumber = checkService.validatePhoneNumber(phoneNumber);
        checkService.checkPhoneNumberParentAlreadyAdded(getAll(), phoneNumber);
        parent.setPhoneNumber(phoneNumber);

        checkService.checkStatus(pet.getStatus());
        pet.setStatus(ADOPTED);

        log.info("ДОБАВЛЕН НОВЫЙ УСЫНОВИТЕЛЬ {} {} {} {}", firstName, lastName, phoneNumber, petId);
        return repository.save(parent);
    }

    @Override
    public Parent getById(Long id) {
        checkService.checkValue(id);
        log.info("ПОЛУЧЕН УСЫНОВИТЕЛЬ {}", id);
        return repository.findById(id).orElseThrow(ParentNotFoundException::new);
    }

    @Override
    public Collection<Parent> getAllByParameters(String firstName, String lastName, String phoneNumber) {

        if (firstName != null) {
            checkService.checkName(firstName);
            log.info("ПОЛУЧЕНЫ УСЫНОВИТЕЛИ ПО ИМЕНИ {}", firstName);
            return repository.getAllByFirstNameContainsIgnoreCase(firstName);

        } else if (lastName != null) {
            checkService.checkName(lastName);
            log.info("ПОЛУЧЕНЫ УСЫНОВИТЕЛИ ПО ФАМИЛИИ {}", lastName);
            return repository.getAllByLastNameContainsIgnoreCase(lastName);

        } else if (phoneNumber != null) {
            log.info("ПОЛУЧЕНЫ УСЫНОВИТЕЛИ ПО НОМЕРУ ТЕЛЕФОНА {}", phoneNumber);
            return repository.getAllByPhoneNumberContains(phoneNumber);

        } else {
            return getAll();
        }
    }

    @Override
    public Collection<Parent> getAll() {
        log.info("ПОЛУЧЕНЫ ВСЕ УСЫНОВИТЕЛИ");
        return repository.findAll();
    }

    @Override
    public Parent edit(Long id,
                       String firstName,
                       String lastName,
                       String phoneNumber,
                       Long petId) {

        Parent parent = getById(id);

        if (firstName == null & lastName == null & phoneNumber == null & petId == null) {
            return parent;

        } else {

            Parent edit = new Parent(parent.getFirstName(), parent.getLastName(), parent.getPhoneNumber(), parent.getPet());
            edit.setId(parent.getId());

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
                checkService.checkPhoneNumberParentAlreadyAdded(getAll(), phoneNumber);
                edit.setPhoneNumber(phoneNumber);
            }

            if (petId != null) {
                Pet pet = petService.getById(petId);
                checkService.checkStatus(pet.getStatus());
                pet.setStatus(ADOPTED);
                edit.getPet().setStatus(FREE);
                edit.setPet(pet);
            }

            log.info("ИЗМЕНЕНЫ ДАННЫЕ УСЫНОВИТЕЛЯ {}", id);
            return repository.save(edit);
        }
    }

    @Override
    public Parent delete(Long id) {
        Parent parent = getById(id);
        repository.delete(parent);
        log.info("УДАЛЕН УСЫНОВИТЕЛЬ {}", id);
        return parent;
    }
}
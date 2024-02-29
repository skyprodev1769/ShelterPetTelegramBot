package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link PetServiceImpl}
 * является сервисным классом для контроллера {@link PetController}
 */
@Slf4j
@Service
public class PetServiceImpl implements PetService {

    private final PetRepository repository;
    private final CheckService checkService;
    private final ShelterService shelterService;

    public PetServiceImpl(PetRepository repository,
                          CheckService checkService,
                          ShelterService shelterService) {

        this.repository = repository;
        this.checkService = checkService;
        this.shelterService = shelterService;
    }

    @Override
    public Pet add(PetType type,
                   PetStatus status,
                   String name,
                   Long shelterId) {

        Shelter shelter = shelterService.getById(shelterId);
        Pet pet = new Pet(type, status, name, shelter);
        checkService.checkPet(type, shelter.getType(), name, pet, getAll());

        log.info("ДОБАВЛЕНО НОВОЕ ЖИВОТНОЕ: \"тип\" - {}; \"статус\" - {}; \"имя\" - {}; \"ID приюта для животных\" - {}", type, status, name, shelterId);
        return repository.save(pet);
    }

    @Override
    public Pet getById(Long id) {
        checkService.checkValue(id);
        log.info("ПОЛУЧЕНО ЖИВОТНОЕ ПО ID - {}", id);
        return repository.findById(id).orElseThrow(PetNotFoundException::new);
    }

    @Override
    public Collection<Pet> getAllByParameters(PetType type,
                                              PetStatus status,
                                              String name,
                                              Long shelterId) {

        if (type != null) {
            log.info("ПОЛУЧЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {}", type);
            return repository.getAllByType(type);

        } else if (status != null) {
            log.info("ПОЛУЧЕНЫ ЖИВОТНЫЕ ПО СТАТУСУ - {}", status);
            return repository.getAllByStatus(status);

        } else if (name != null) {
            checkService.checkName(name);
            log.info("ПОЛУЧЕНЫ ЖИВОТНЫЕ ПО ИМЕНИ - {}", name);
            return repository.getAllByNameContainsIgnoreCase(name);

        } else if (shelterId != null) {
            checkService.checkValue(shelterId);
            log.info("ПОЛУЧЕНЫ ЖИВОТНЫЕ ПО ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", shelterId);
            return repository.getAllByShelterId(shelterId);

        } else {
            return getAll();
        }
    }

    @Override
    public Collection<Pet> getAll() {
        log.info("ПОЛУЧЕНЫ ВСЕ ЖИВОТНЫЕ");
        return repository.findAll();
    }

    @Override
    public Pet edit(Long id, PetType type, PetStatus status, String name, Long shelterId) {

        Pet pet = getById(id);

        if (type == null & status == null & name == null & shelterId == null) {
            return pet;

        } else {

            Pet edit = new Pet(pet.getType(), pet.getStatus(), pet.getName(), pet.getShelter());
            edit.setId(pet.getId());

            if (type != null) {
                edit.setType(type);
                log.info("ИЗМЕНЕН ТИП ЖИВОТНОГО ПО ID - {} НА \"тип\" - {}", id, type);
            }

            if (status != null) {
                edit.setStatus(status);
                log.info("ИЗМЕНЕН СТАТУС ЖИВОТНОГО ПО ID - {} НА \"статус\" - {}", id, status);
            }

            if (name != null) {
                checkService.checkName(name);
                edit.setName(name);
                checkService.checkPetAlreadyAdded(getAll(), edit);
                log.info("ИЗМЕНЕНО ИМЯ ЖИВОТНОГО ПО ID - {} НА \"имя\" - {}", id, name);
            }

            if (shelterId != null) {
                Shelter shelter = shelterService.getById(shelterId);
                edit.setShelter(shelter);
                log.info("ИЗМЕНЕН ID ПРИЮТА ДЛЯ ЖИВОТНОГО ПО ID - {} НА \"ID приюта для животных\" - {}", id, shelterId);
            }

            checkService.checkTypes(edit.getType(), edit.getShelter().getType());
            return repository.save(edit);
        }
    }

    @Override
    public Pet delete(Long id) {
        Pet pet = getById(id);
        repository.delete(pet);
        log.info("УДАЛЕНО ЖИВОТНОЕ ПО ID - {}", id);
        return pet;
    }
}
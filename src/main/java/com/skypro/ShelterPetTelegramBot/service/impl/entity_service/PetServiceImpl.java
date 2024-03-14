package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_ID;
import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_PET;

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

        checkService.checkName(name);

        Shelter shelter = shelterService.getById(shelterId);
        checkService.checkTypes(type, shelter.getType());

        Pet pet = new Pet(type, status, name, shelter);

        checkService.checkPetAlreadyAdded(getAll(), pet);

        log.info("СОЗДАНО НОВОЕ ЖИВОТНОЕ: \"тип\" - {}; \"статус\" - {}; \"имя\" - {}; \"ID приюта для животных\" - {}", type, status, name, shelterId);
        return repository.save(pet);
    }

    @Override
    @Cacheable(value = CACHE_PET)
    public Pet getById(Long id) {
        checkService.checkValue(id);
        log.info("ЗАПРОШЕНО ЖИВОТНОЕ ПО ID - {}", id);
        return repository.findById(id).orElseThrow(PetNotFoundException::new);
    }

    @Override
    @Cacheable(value = CACHE_PET)
    public Collection<Pet> getAllByParameters(PetType type,
                                              PetStatus status,
                                              String name,
                                              Long shelterId) {

        if (type == null & status == null & name == null & shelterId == null) {
            return getAll();

        } else {

            if (type != null & status == null & name == null & shelterId == null) {
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {}", type);
                return repository.getAllByType(type);

            } else if (type == null & status != null & name == null & shelterId == null) {
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО СТАТУСУ - {}", status);
                return repository.getAllByStatus(status);

            } else if (type == null & status == null & name != null & shelterId == null) {
                checkService.checkName(name);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ИМЕНИ - {}", name);
                return repository.getAllByNameContainsIgnoreCase(name);

            } else if (type == null & status == null & name == null & shelterId != null) {
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", shelterId);
                return repository.getAllByShelterId(shelterId);

            } else if (type != null & status != null & name == null & shelterId == null) {
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {} И СТАТУСУ - {}", type, status);
                return repository.getAllByTypeAndStatus(type, status);

            } else if (type != null & status == null & name != null & shelterId == null) {
                checkService.checkName(name);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {} И ИМЕНИ - {}", type, name);
                return repository.getAllByTypeAndNameContainsIgnoreCase(type, name);

            } else if (type != null & status == null & name == null & shelterId != null) {
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", type, shelterId);
                return repository.getAllByTypeAndShelterId(type, shelterId);

            } else if (type == null & status != null & name != null & shelterId == null) {
                checkService.checkName(name);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО СТАТУСУ - {} И ИМЕНИ - {}", status, name);
                return repository.getAllByStatusAndNameContainsIgnoreCase(status, name);

            } else if (type == null & status != null & name == null & shelterId != null) {
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО СТАТУСУ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", status, shelterId);
                return repository.getAllByStatusAndShelterId(status, shelterId);

            } else if (type == null & status == null & name != null & shelterId != null) {
                checkService.checkName(name);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ИМЕНИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", name, shelterId);
                return repository.getAllByNameContainsIgnoreCaseAndShelterId(name, shelterId);

            } else if (type != null & status != null & name != null & shelterId == null) {
                checkService.checkName(name);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {}, СТАТУСУ - {} И ИМЕНИ - {}", type, status, name);
                return repository.getAllByTypeAndStatusAndNameContainsIgnoreCase(type, status, name);

            } else if (type != null & status != null & name == null) {
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {}, СТАТУСУ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", type, status, shelterId);
                return repository.getAllByTypeAndStatusAndShelterId(type, status, shelterId);

            } else if (type != null & status == null) {
                checkService.checkName(name);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {}, ИМЕНИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", type, name, shelterId);
                return repository.getAllByTypeAndNameContainsIgnoreCaseAndShelterId(type, name, shelterId);

            } else if (type == null) {
                checkService.checkName(name);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО СТАТУСУ - {}, ИМЕНИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", status, name, shelterId);
                return repository.getAllByStatusAndNameContainsIgnoreCaseAndShelterId(status, name, shelterId);

            } else {
                checkService.checkName(name);
                checkService.checkValue(shelterId);
                log.info("ЗАПРОШЕНЫ ЖИВОТНЫЕ ПО ТИПУ - {}, СТАТУСУ - {}, ИМЕНИ - {} И ID ПРИЮТА ДЛЯ ЖИВОТНЫХ - {}", type, status, name, shelterId);
                return repository.getAllByTypeAndStatusAndNameContainsIgnoreCaseAndShelterId(type, status, name, shelterId);
            }
        }
    }

    @Override
    @Cacheable(value = CACHE_PET)
    public Collection<Pet> getAll() {
        log.info("ЗАПРОШЕНЫ ВСЕ ЖИВОТНЫЕ");
        return repository.findAll();
    }

    @Override
    @CachePut(value = CACHE_PET, key = CACHE_ID)
    public Pet edit(Long id, PetType type, PetStatus status, String name, Long shelterId) {

        Pet pet = getById(id);

        if (type == null & status == null & name == null & shelterId == null) {
            return pet;

        } else {

            Pet edit = new Pet(pet.getType(), pet.getStatus(), pet.getName(), pet.getShelter());
            edit.setId(pet.getId());

            if (type != null && shelterId == null) {
                checkService.checkTypes(type, edit.getShelter().getType());
            }

            if (type == null && shelterId != null) {
                Shelter shelter = shelterService.getById(shelterId);
                checkService.checkTypes(edit.getType(), shelter.getType());
            }

            if (type != null && shelterId != null) {
                Shelter shelter = shelterService.getById(shelterId);
                checkService.checkTypes(type, shelter.getType());
                edit.setType(type);
                edit.setShelter(shelter);
                log.info("ИЗМЕНЕН ТИП ЖИВОТНОГО И ID ПРИЮТА ДЛЯ ЖИВОТНОГО ПО ID - {} НА \"тип\" - {} И \"ID приюта для животных\" - {}", id, type, shelterId);
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

            return repository.save(edit);
        }
    }

    @Override
    @CacheEvict(value = CACHE_PET)
    public Pet delete(Long id) {
        Pet pet = getById(id);
        repository.delete(pet);
        log.info("УДАЛЕНО ЖИВОТНОЕ ПО ID - {}", id);
        return pet;
    }
}
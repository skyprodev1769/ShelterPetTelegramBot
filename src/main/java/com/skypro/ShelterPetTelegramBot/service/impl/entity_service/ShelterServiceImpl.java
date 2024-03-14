package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.repository.ShelterRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_ID;
import static com.skypro.ShelterPetTelegramBot.utils.Cache.CACHE_SHELTER;

/**
 * Класс {@link ShelterServiceImpl}
 * является сервисным классом для контроллера {@link ShelterController}
 */
@Slf4j
@Service
public class ShelterServiceImpl implements ShelterService {

    private final ShelterRepository repository;
    private final CheckService checkService;

    public ShelterServiceImpl(ShelterRepository repository, CheckService checkService) {
        this.repository = repository;
        this.checkService = checkService;
    }

    @Override
    public Shelter add(PetType type, String address) {
        checkService.checkAddress(address);
        Shelter shelter = new Shelter(type, address);
        checkService.checkShelterAlreadyAdded(getAll(), shelter);

        log.info("СОЗДАН НОВЫЙ ПРИЮТ: \"тип\" - {}; \"адрес\" - {}", type, address);
        return repository.save(shelter);
    }

    @Override
    @Cacheable(value = CACHE_SHELTER)
    public Shelter getById(Long id) {
        checkService.checkValue(id);
        log.info("ЗАПРОШЕН ПРИЮТ ПО ID - {}", id);
        return repository.findById(id).orElseThrow(ShelterNotFoundException::new);
    }

    @Override
    @Cacheable(value = CACHE_SHELTER)
    public Collection<Shelter> getAllByParameters(PetType type, String address) {

        if (type == null & address == null) {
            return getAll();

        } else {

            if (type != null & address == null) {
                log.info("ЗАПРОШЕНЫ ПРИЮТЫ ПО ТИПУ - {}", type);
                return repository.getAllByType(type);

            } else if (type == null) {
                checkService.checkAddress(address);
                log.info("ЗАПРОШЕНЫ ПРИЮТЫ ПО АДРЕСУ - {}", address);
                return repository.getAllByAddressContainsIgnoreCase(address);

            } else {
                checkService.checkAddress(address);
                log.info("ЗАПРОШЕНЫ ПРИЮТЫ ПО ТИПУ - {} И ПО АДРЕСУ - {}", type, address);
                return repository.getAllByTypeAndAddressContainsIgnoreCase(type, address);
            }
        }
    }

    @Override
    @Cacheable(value = CACHE_SHELTER)
    public Collection<Shelter> getAll() {
        log.info("ЗАПРОШЕНЫ ВСЕ ПРИЮТЫ");
        return repository.findAll();
    }

    @Override
    @CachePut(value = CACHE_SHELTER, key = CACHE_ID)
    public Shelter edit(Long id, PetType type, String address) {

        Shelter shelter = getById(id);

        if (type == null & address == null) {
            return shelter;

        } else {

            Shelter edit = new Shelter(shelter.getType(), shelter.getAddress());
            edit.setId(shelter.getId());

            if (type != null) {
                edit.setType(type);
                log.info("ИЗМЕНЕН ТИП ПРИЮТА ПО ID - {} НА \"тип\" - {}", id, type);
            }

            if (address != null) {
                checkService.checkAddress(address);
                edit.setAddress(address);
                log.info("ИЗМЕНЕН АДРЕС ПРИЮТА ПО ID - {} НА \"адрес\" - {}", id, address);
                checkService.checkShelterAlreadyAdded(getAll(), edit);
            }

            return repository.save(edit);
        }
    }

    @Override
    @CacheEvict(CACHE_SHELTER)
    public Shelter delete(Long id) {
        Shelter shelter = getById(id);
        repository.delete(shelter);
        log.info("УДАЛЕН ПРИЮТ ПО ID - {}", id);
        return shelter;
    }
}
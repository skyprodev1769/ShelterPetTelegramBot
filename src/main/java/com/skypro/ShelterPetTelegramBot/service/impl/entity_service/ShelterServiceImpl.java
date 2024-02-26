package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.repository.ShelterRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link ShelterServiceImpl}
 * является сервисным классом для контроллера {@link ShelterController}
 */
@Service
public class ShelterServiceImpl implements ShelterService {

    @Autowired
    ShelterRepository repository;
    @Autowired
    CheckService checkService;

    @Override
    public Shelter add(PetType type, String address) {
        Shelter shelter = new Shelter(type, address);
        checkService.checkShelter(address, shelter, getAll());
        return repository.save(shelter);
    }

    @Override
    public Shelter get(Long id) {
        checkService.validateLong(id);
        return repository.findById(id).orElseThrow(ShelterNotFoundException::new);
    }

    @Override
    public Collection<Shelter> getAll() {
        return repository.findAll();
    }

    @Override
    public Shelter edit(Long id, PetType type, String address) {

        Shelter shelter = get(id);

        if (type == null & address == null) {
            return shelter;

        } else {

            Shelter edit = new Shelter(shelter.getType(), shelter.getAddress());

            if (type != null) {
                edit.setType(type);
            }

            if (address != null) {
                edit.setAddress(address);
            }

            edit.setId(shelter.getId());
            checkService.checkShelter(edit.getAddress(), edit, getAll());
            return repository.save(edit);
        }
    }

    @Override
    public Shelter delete(Long id) {
        Shelter shelter = get(id);
        repository.delete(shelter);
        return shelter;
    }
}
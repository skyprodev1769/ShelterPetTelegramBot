package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Класс {@link PetServiceImpl}
 * является сервисным классом для контроллера {@link PetController}
 */
@Service
public class PetServiceImpl implements PetService {

    @Autowired
    PetRepository repository;
    @Autowired
    CheckService checkService;
    @Autowired
    ShelterService shelterService;

    @Override
    public Pet add(PetType type, String name, Long shelterId) {
        Shelter shelter = shelterService.getById(shelterId);
        Pet pet = new Pet(type, name, shelter);
        checkService.checkPet(type, shelter.getType(), name, pet, getAll());
        return repository.save(pet);
    }

    @Override
    public Pet getById(Long id) {
        checkService.validateLong(id);
        return repository.findById(id).orElseThrow(PetNotFoundException::new);
    }

    @Override
    public Collection<Pet> getAllByParameters(String name, PetType type, Long shelterId) {

        if (name != null) {
            checkService.validateName(name);
            return repository.getAllByNameContainsIgnoreCase(name);

        } else if (type != null) {
            return repository.getAllByType(type);

        } else if (shelterId != null) {
            checkService.validateLong(shelterId);
            return repository.getAllByShelterId(shelterId);

        } else {
            return getAll();
        }
    }

    @Override
    public Collection<Pet> getAll() {
        return repository.findAll();
    }

    @Override
    public Pet edit(Long id, PetType type, String name, Long shelterId) {

        Pet pet = getById(id);

        if (type == null & name == null & shelterId == null) {
            return pet;

        } else {

            Pet edit = new Pet(pet.getType(), pet.getName(), pet.getShelter());

            if (type != null) {
                edit.setType(type);
            }

            if (name != null) {
                edit.setName(name);
            }

            if (shelterId != null) {
                Shelter shelter = shelterService.getById(shelterId);
                edit.setShelter(shelter);
            }

            edit.setId(pet.getId());
            checkService.checkPet(edit.getType(), edit.getShelter().getType(), edit.getName(), edit, getAll());
            return repository.save(edit);
        }
    }

    @Override
    public Pet delete(Long id) {
        Pet pet = getById(id);
        repository.delete(pet);
        return pet;
    }
}
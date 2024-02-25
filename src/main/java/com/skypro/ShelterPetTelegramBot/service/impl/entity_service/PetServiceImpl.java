package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
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
    ShelterService shelterService;

    @Override
    public Pet add(PetType type, String name, Long shelterId) {
        Shelter shelter = shelterService.get(shelterId);
        Pet pet = new Pet(type, name, shelter);
        return repository.save(pet);
    }

    @Override
    public Pet get(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Collection<Pet> getAll() {
        return repository.findAll();
    }

    @Override
    public Pet edit(Long id, PetType type, String name, Long shelterId) {
        Pet pet = get(id);
        Shelter shelter = shelterService.get(shelterId);

        pet.setType(type);
        pet.setName(name);
        pet.setShelter(shelter);

        return repository.save(pet);
    }

    @Override
    public Pet delete(Long id) {
        Pet pet = get(id);
        repository.delete(pet);
        return pet;
    }
}
package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Интерфейс {@link PetRepository}
 * является репозиторием для сущности {@link Pet}
 */
public interface PetRepository extends JpaRepository<Pet, Long> {

    Collection<Pet> getAllByType(PetType type);

    Collection<Pet> getAllByNameContainsIgnoreCase(String name);

    Collection<Pet> getAllByShelterId(Long shelterId);
}
package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс {@link PetRepository}
 * является репозиторием для сущности {@link Pet}
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Optional<Pet> getByNameContainsIgnoreCase(String name);

    Collection<Pet> getAllByType(PetType type);

    Collection<Pet> getAllByStatus(PetStatus status);

    Collection<Pet> getAllByNameContainsIgnoreCase(String name);

    Collection<Pet> getAllByShelterId(Long shelterId);
}
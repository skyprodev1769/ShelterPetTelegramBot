package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
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

    Collection<Pet> getAllByTypeAndStatus(PetType type, PetStatus status);

    Collection<Pet> getAllByTypeAndNameContainsIgnoreCase(PetType type, String name);

    Collection<Pet> getAllByTypeAndShelterId(PetType type, Long shelterId);

    Collection<Pet> getAllByStatusAndNameContainsIgnoreCase(PetStatus status, String name);

    Collection<Pet> getAllByStatusAndShelterId(PetStatus status, Long shelterId);

    Collection<Pet> getAllByNameContainsIgnoreCaseAndShelterId(String name, Long shelterId);

    Collection<Pet> getAllByTypeAndStatusAndNameContainsIgnoreCase(PetType type, PetStatus status, String name);

    Collection<Pet> getAllByTypeAndStatusAndShelterId(PetType type, PetStatus status, Long shelterId);

    Collection<Pet> getAllByTypeAndNameContainsIgnoreCaseAndShelterId(PetType type, String name, Long shelterId);

    Collection<Pet> getAllByStatusAndNameContainsIgnoreCaseAndShelterId(PetStatus status, String name, Long shelterId);

    Collection<Pet> getAllByTypeAndStatusAndNameContainsIgnoreCaseAndShelterId(PetType type, PetStatus status, String name, Long shelterId);
}
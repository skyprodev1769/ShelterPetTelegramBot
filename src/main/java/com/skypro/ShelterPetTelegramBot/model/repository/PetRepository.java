package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс {@link PetRepository}
 * является репозиторием для сущности {@link Pet}
 */
public interface PetRepository extends JpaRepository<Pet, Long> {
}
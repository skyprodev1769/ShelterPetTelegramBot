package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс {@link ShelterRepository}
 * является репозиторием для сущности {@link Shelter}
 */
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
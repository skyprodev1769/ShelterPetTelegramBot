package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Интерфейс {@link ShelterRepository}
 * является репозиторием для сущности {@link Shelter}
 */
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    Collection<Shelter> getAllByType(PetType type);

    Collection<Shelter> getAllByAddressContainsIgnoreCase(String address);
}
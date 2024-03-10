package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Интерфейс {@link ShelterRepository}
 * является репозиторием для сущности {@link Shelter}
 */
@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    Collection<Shelter> getAllByType(PetType type);

    Collection<Shelter> getAllByAddressContainsIgnoreCase(String address);

    Collection<Shelter> getAllByTypeAndAddressContainsIgnoreCase(PetType type, String address);
}
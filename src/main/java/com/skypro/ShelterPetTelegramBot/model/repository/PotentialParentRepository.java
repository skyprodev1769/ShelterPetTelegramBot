package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.PotentialParent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс {@link PotentialParentRepository}
 * является репозиторием для сущности {@link PotentialParent}
 */
public interface PotentialParentRepository extends JpaRepository<PotentialParent, Long> {
}
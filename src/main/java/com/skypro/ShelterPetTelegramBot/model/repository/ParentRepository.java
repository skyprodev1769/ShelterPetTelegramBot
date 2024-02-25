package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс {@link ParentRepository}
 * является репозиторием для сущности {@link Parent}
 */
public interface ParentRepository extends JpaRepository<Parent, Long> {
}
package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс {@link VolunteerRepository}
 * является репозиторием для сущности {@link Volunteer}
 */
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
}
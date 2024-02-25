package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс {@link UserRepository}
 * является репозиторием для сущности {@link User}
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс {@link UserRepository}
 * является репозиторием для сущности {@link User}
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
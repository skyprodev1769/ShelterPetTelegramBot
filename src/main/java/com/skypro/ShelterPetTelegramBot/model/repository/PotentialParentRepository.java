package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс {@link PotentialParentRepository}
 * является репозиторием для сущности {@link PotentialParent}
 */
@Repository
public interface PotentialParentRepository extends JpaRepository<PotentialParent, Long> {

    PotentialParent getByPhoneNumber(String phoneNumber);

    List<PotentialParent> getAllByChatId(Long chatId);
}
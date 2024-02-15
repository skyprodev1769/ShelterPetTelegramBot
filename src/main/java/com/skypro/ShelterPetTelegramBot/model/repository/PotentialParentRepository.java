package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.PotentialParent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PotentialParentRepository extends JpaRepository<PotentialParent, Long> {
}
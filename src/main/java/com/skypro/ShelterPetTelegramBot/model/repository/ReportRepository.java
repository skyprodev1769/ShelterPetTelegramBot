package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс {@link ReportRepository}
 * является репозиторием для сущности {@link Report}
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByParentFirstNameContainsIgnoreCaseAndParentLastNameContainsIgnoreCase(String parentFirstName, String parentLastName);
}
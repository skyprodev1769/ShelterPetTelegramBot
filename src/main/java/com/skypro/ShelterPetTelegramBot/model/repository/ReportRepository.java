package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс {@link ReportRepository}
 * является репозиторием для сущности {@link Report}
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Report getByParentFirstNameContainsIgnoreCaseAndParentLastNameContainsIgnoreCaseAndParentPetNameContainsIgnoreCaseAndDate(String firstName,
                                                                                                                              String lastName,
                                                                                                                              String petName,
                                                                                                                              LocalDate date);

    Collection<Report> getByParentFirstNameContainsIgnoreCaseAndParentLastNameContainsIgnoreCaseAndParentPetNameContainsIgnoreCase(String firstName,
                                                                                                                                   String lastName,
                                                                                                                                   String petName);

    Optional<Report> getByDate(LocalDate date);

    Collection<Report> getAllByDate(LocalDate date);

    Collection<Report> getAllByStatus(ReportStatus status);

    Collection<Report> getAllByDateAndStatus(LocalDate date, ReportStatus status);
}
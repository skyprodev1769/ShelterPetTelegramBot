package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Интерфейс {@link VolunteerRepository}
 * является репозиторием для сущности {@link Volunteer}
 */
@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCase(String firstName);

    Collection<Volunteer> getAllByLastNameContainsIgnoreCase(String lastName);

    Collection<Volunteer> getAllByPhoneNumberContains(String phoneNumber);

    Collection<Volunteer> getAllByShelterId(Long shelterId);
}
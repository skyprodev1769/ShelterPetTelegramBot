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

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(String firstName, String lastName);

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(String firstName, String phoneNumber);

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCaseAndShelterId(String firstName, Long shelterId);

    Collection<Volunteer> getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(String lastName, String phoneNumber);

    Collection<Volunteer> getAllByLastNameContainsIgnoreCaseAndShelterId(String lastName, Long shelterId);

    Collection<Volunteer> getAllByPhoneNumberContainsAndShelterId(String phoneNumber, Long shelterId);

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(String firstName, String lastName, String phoneNumber);

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(String firstName, String lastName, Long shelterId);

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(String firstName, String phoneNumber, Long shelterId);

    Collection<Volunteer> getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(String lastName, String phoneNumber, Long shelterId);

    Collection<Volunteer> getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(String firstName, String lastName, String phoneNumber, Long shelterId);
}
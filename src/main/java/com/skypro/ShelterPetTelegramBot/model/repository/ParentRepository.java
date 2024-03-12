package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс {@link ParentRepository}
 * является репозиторием для сущности {@link Parent}
 */
@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {

    Parent getByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(String firstName, String lastName);

    Optional<Parent> getByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPetNameContainsIgnoreCase(String firstName,
                                                                                                              String lastName,
                                                                                                              String petName);

    Collection<Parent> getAllByFirstNameContainsIgnoreCase(String firstName);

    Collection<Parent> getAllByLastNameContainsIgnoreCase(String lastName);

    Collection<Parent> getAllByPhoneNumberContains(String phoneNumber);

    Collection<Parent> getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(String firstName, String lastName);

    Collection<Parent> getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(String firstName, String phoneNumber);

    Collection<Parent> getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(String lastName, String phoneNumber);

    Collection<Parent> getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(String firstName, String lastName, String phoneNumber);
}
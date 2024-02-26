package com.skypro.ShelterPetTelegramBot.model.repository;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/**
 * Интерфейс {@link ParentRepository}
 * является репозиторием для сущности {@link Parent}
 */
public interface ParentRepository extends JpaRepository<Parent, Long> {

    Collection<Parent> getAllByFirstNameContainsIgnoreCase(String firstName);

    Collection<Parent> getAllByLastNameContainsIgnoreCase(String lastName);

    Collection<Parent> getAllByPhoneNumberContains(String phoneNumber);

    Collection<Parent> getAllByVolunteerId(Long volunteer);
}
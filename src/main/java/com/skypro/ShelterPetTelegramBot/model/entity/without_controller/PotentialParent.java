package com.skypro.ShelterPetTelegramBot.model.entity.without_controller;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс {@link PotentialParent}
 * является сущностью для хранения потенциальных усыновителей в БД
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "potential_parent")
public class PotentialParent {

    @Id
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
}
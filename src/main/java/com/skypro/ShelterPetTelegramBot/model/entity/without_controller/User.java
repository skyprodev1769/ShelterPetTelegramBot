package com.skypro.ShelterPetTelegramBot.model.entity.without_controller;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Класс {@link User}
 * является сущностью для хранения новых пользователей в БД
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "register_at")
    private Timestamp registerAt;
}
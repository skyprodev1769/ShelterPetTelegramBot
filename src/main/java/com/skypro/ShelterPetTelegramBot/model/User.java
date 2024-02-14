package com.skypro.ShelterPetTelegramBot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Класс {@link User}
 * является сущностью для записи новых пользователей в БД и имеет 3 поля: <br>
 * <b> {@code Long chatId} </b> <br> <i> является идентификатором пользователя (его id в telegram) </i> <br>
 * <b> {@code String firstName} </b> <br> <i> является именем пользователя </i> <br>
 * <b> {@code Timestamp registerAt} </b> <br> <i> является временем регистрации пользователя </i>
 */

@Data
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
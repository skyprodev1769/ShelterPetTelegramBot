package com.skypro.ShelterPetTelegramBot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Класс {@link PotentialParent}
 * <p>
 * является сущностью для хранения потенциальных усыновителей в БД и имеет 4 поля: <br> <br>
 *
 * <b> {@code Long chatId} </b> <br> <i> является идентификатором пользователя (его id в telegram) </i> <br> <br>
 * <b> {@code String firstName} </b> <br> <i> является именем усыновителя </i> <br> <br>
 * <b> {@code String lastName} </b> <br> <i> является фамилией усыновителя </i> <br> <br>
 * <b> {@code String phoneNumber} </b> <br> <i> является номером телефона усыновителя </i>
 */

@Data
@Entity(name = "potential_parent")
public class PotentialParent {

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;
}
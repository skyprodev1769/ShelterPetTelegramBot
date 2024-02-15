package com.skypro.ShelterPetTelegramBot.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс {@link PotentialParent}
 * является сущностью для записи потенциальных усыновителей в БД и имеет 4 поля: <br>
 * <b> {@code Long id} </b> <br> <i> является идентификатором в БД </i> <br>
 * <b> {@code String firstName} </b> <br> <i> является именем усыновителя </i> <br>
 * <b> {@code String lastName} </b> <br> <i> является фамилией усыновителя </i>
 * <b> {@code String phoneNumber} </b> <br> <i> является номером телефона усыновителя </i>
 */

@Data
@Entity(name = "potential_parent")
public class PotentialParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;
}
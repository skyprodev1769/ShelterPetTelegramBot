package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс {@link Shelter}
 * является сущностью для хранения приютов для животных в БД и имеет 3 поля: <br> <br>
 *
 * <b> {@code Long id} </b> <br> <i> является идентификатором приюта </i> <br> <br>
 * <b> {@code PetType type} </b> <br> <i> является указателем типа животных в приюте </i> <br> <br>
 * <b> {@code String address} </b> <br> <i> является адресом приюта </i>
 */
@Data
@Entity(name = "shelter")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PetType type;

    @Column(name = "address")
    private String address;

    public Shelter() {
    }

    public Shelter(PetType type, String address) {
        this.type = type;
        this.address = address;
    }
}
package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс {@link Pet}
 * является сущностью для хранения животных в БД
 */
@Data
@Entity(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PetType type;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition")
    private PetStatus status;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public Pet() {
    }

    public Pet(PetType type, String name, PetStatus status, Shelter shelter) {
        this.type = type;
        this.name = name;
        this.status = status;
        this.shelter = shelter;
    }
}
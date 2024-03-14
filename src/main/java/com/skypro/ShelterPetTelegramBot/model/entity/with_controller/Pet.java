package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс {@link Pet}
 * является сущностью для хранения животных в БД
 */
@Data
@NoArgsConstructor
@Entity(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PetType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PetStatus status;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public Pet(PetType type, PetStatus status, String name, Shelter shelter) {
        this.type = type;
        this.status = status;
        this.name = name;
        this.shelter = shelter;
    }
}
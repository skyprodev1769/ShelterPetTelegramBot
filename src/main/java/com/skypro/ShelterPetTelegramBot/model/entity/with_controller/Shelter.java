package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Класс {@link Shelter}
 * является сущностью для хранения приютов для животных в БД
 */
@Data
@NoArgsConstructor
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

    @JsonIgnore
    @OneToMany(mappedBy = "shelter")
    private Collection<Volunteer> volunteers;

    @JsonIgnore
    @OneToMany(mappedBy = "shelter")
    private Collection<Pet> pets;

    public Shelter(PetType type, String address) {
        this.type = type;
        this.address = address;
    }
}
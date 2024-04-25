package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс {@link Parent}
 * является сущностью для хранения усыновителей в БД
 */
@Data
@NoArgsConstructor
@Entity(name = "parent")
public class Parent {

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

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public Parent(String firstName, String lastName, String phoneNumber, Pet pet) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.pet = pet;
    }
}
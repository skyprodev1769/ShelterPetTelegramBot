package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс {@link Parent}
 * является сущностью для хранения усыновителей в БД
 */
@Data
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

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public Parent() {
    }

    public Parent(String firstName, String lastName, String phoneNumber, Volunteer volunteer, Pet pet) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.volunteer = volunteer;
        this.pet = pet;
    }
}
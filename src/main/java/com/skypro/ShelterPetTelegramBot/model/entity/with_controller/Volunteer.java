package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс {@link Volunteer}
 * является сущностью для хранения волонтеров в БД
 */
@Data
@NoArgsConstructor
@Entity(name = "volunteer")
public class Volunteer {

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
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public Volunteer(String firstName, String lastName, String phoneNumber, Shelter shelter) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.shelter = shelter;
    }
}
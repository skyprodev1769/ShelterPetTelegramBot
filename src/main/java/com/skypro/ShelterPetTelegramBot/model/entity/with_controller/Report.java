package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetHealth;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс {@link Report}
 * является сущностью для хранения отчетов о животных в БД
 */
@Data
@Entity(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Transient
    private long fileSize;

    @Transient
    private String mediaType;

    @JsonIgnore
    @Transient
    private byte[] data;

    @Column(name = "pet_diet")
    private String petDiet;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_health")
    private PetHealth petHealth;

    @Column(name = "pet_behavior")
    private String petBehavior;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
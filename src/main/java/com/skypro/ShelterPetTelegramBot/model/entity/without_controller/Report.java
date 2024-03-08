package com.skypro.ShelterPetTelegramBot.model.entity.without_controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Класс {@link Report}
 * является сущностью для хранения отчетов о животных в БД
 */
@Data
@Entity(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "photo")
    private String photo;

    @Column(name = "document")
    private String document;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
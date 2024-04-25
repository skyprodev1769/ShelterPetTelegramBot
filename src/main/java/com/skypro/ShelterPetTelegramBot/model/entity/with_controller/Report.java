package com.skypro.ShelterPetTelegramBot.model.entity.with_controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Класс {@link Report}
 * является сущностью для хранения отчетов о животных в БД
 */
@Data
@NoArgsConstructor
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

    @JsonIgnore
    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReportStatus status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Parent parent;

    public Report(String photo, String document, LocalDate date, ReportStatus status, Parent parent) {
        this.photo = photo;
        this.document = document;
        this.date = date;
        this.status = status;
        this.parent = parent;
    }
}
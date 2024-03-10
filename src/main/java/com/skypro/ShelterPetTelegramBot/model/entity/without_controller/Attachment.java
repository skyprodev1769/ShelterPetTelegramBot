package com.skypro.ShelterPetTelegramBot.model.entity.without_controller;

import com.skypro.ShelterPetTelegramBot.model.enums.FileType;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Класс {@link Attachment}
 * является сущностью для хранения файлов отчетов о животных в БД
 */
@Data
@Entity(name = "file")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private long size;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FileType type;

    @Transient
    private byte[] data;
}
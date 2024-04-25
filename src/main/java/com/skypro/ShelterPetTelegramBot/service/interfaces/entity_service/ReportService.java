package com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.enums.FileType;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.ReportServiceImpl;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Интерфейс {@link ReportService}
 * содержит набор методов для сервисного класса {@link ReportServiceImpl}
 */
public interface ReportService {

    /**
     * Метод находит отчет о животном из БД по id
     *
     * @param id <i> является идентификатором животного </i>
     */
    Report getById(Long id);

    /**
     * Метод возвращает вложенный файл для отчета о животном из БД по id и типу файла
     *
     * @param id       <i> является идентификатором животного </i> <br>
     * @param type     <i> является расширением получаемого вложения </i> <br>
     * @param response <i> является определителем ответа клиенту </i>
     */
    void getAttachmentByIdAndType(Long id, FileType type, HttpServletResponse response);

    /**
     * Метод находит все отчеты о животных из БД по разным параметрам
     *
     * @param date   <i> является датой отправки отчета </i> <br>
     * @param status <i> является статусом отчета </i>
     */
    Collection<Report> getAllByParameters(LocalDate date, ReportStatus status);

    /**
     * Метод находит все отчеты о животных из БД
     */
    Collection<Report> getAll();
}
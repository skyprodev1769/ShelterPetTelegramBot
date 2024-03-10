package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ReportController;
import com.skypro.ShelterPetTelegramBot.exception.ReportNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.enums.FileType;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import com.skypro.ShelterPetTelegramBot.model.repository.ReportRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.model.enums.FileType.PNG;
import static com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus.VIEWED;

/**
 * Класс {@link ReportServiceImpl}
 * является сервисным классом для контроллера {@link ReportController}
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repository;
    private final CheckService checkService;

    public ReportServiceImpl(ReportRepository repository, CheckService checkService) {
        this.repository = repository;
        this.checkService = checkService;
    }

    @Override
    public Report getById(Long id) {
        checkService.checkValue(id);
        log.info("ЗАПРОШЕН ОТЧЕТ ПО ID - {}", id);
        return repository.findById(id).orElseThrow(ReportNotFoundException::new);
    }

    @Override
    public void getAttachmentById(Long id, FileType type, HttpServletResponse response) {
        Report report = getById(id);

        Path path;

        if (type == PNG) {
            path = Path.of(report.getPhoto());
        } else {
            path = Path.of(report.getDocument());
        }

        downloadAttachmentFromDir(path, response);
        log.info("ПОЛУЧЕНО ВЛОЖЕНИЕ ОТЧЕТА ПО ID - {}", id);

        if (report.getPhoto() != null & report.getDocument() != null) {
            report.setStatus(VIEWED);
            repository.save(report);
        }
    }

    @Override
    public Collection<Report> getAllByParameters(LocalDate date, ReportStatus status) {

        if (date == null & status == null) {
            return getAll();

        } else {

            if (date != null & status == null) {
                log.info("ЗАПРОШЕНЫ ОТЧЕТЫ ПО ДАТЕ - {}", date);
                return repository.getAllByDate(date);

            } else if (date == null) {
                log.info("ЗАПРОШЕНЫ ОТЧЕТЫ ПО СТАТУСУ - {}", status);
                return repository.getAllByStatus(status);

            } else {
                log.info("ЗАПРОШЕНЫ ОТЧЕТЫ ПО ДАТЕ - {} И СТАТУСУ - {}", date, status);
                return repository.getAllByDateAndStatus(date, status);
            }
        }
    }

    @Override
    public Collection<Report> getAll() {
        log.info("ЗАПРОШЕНЫ ВСЕ ОТЧЕТЫ О ЖИВОТНЫХ");
        return repository.findAll();
    }

    private void downloadAttachmentFromDir(Path path, HttpServletResponse response) {

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(Files.probeContentType(path));
            is.transferTo(os);

        } catch (IOException e) {
            log.error("ОШИБКА ЗАГРУЗКИ ВЛОЖЕНИЯ: {}", e.getMessage());
        }
    }
}
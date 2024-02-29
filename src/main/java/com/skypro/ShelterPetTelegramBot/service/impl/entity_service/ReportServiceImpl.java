package com.skypro.ShelterPetTelegramBot.service.impl.entity_service;

import com.skypro.ShelterPetTelegramBot.controller.ReportController;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetHealth;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.ReportRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Класс {@link ReportServiceImpl}
 * является сервисным классом для контроллера {@link ReportController}
 */
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ParentRepository parentRepository;
    private final String avatarsDir;

    public ReportServiceImpl(ReportRepository reportRepository,
                             ParentRepository parentRepository,
                             @Value("${pets.path-to-photo}") String avatarsDir) {

        this.reportRepository = reportRepository;
        this.parentRepository = parentRepository;
        this.avatarsDir = avatarsDir;
    }

    @Override
    public ResponseEntity<String> uploadReport(String parentFirstName,
                                               String parentLastName,
                                               MultipartFile photo,
                                               String diet,
                                               PetHealth health,
                                               String behavior) throws IOException {

        Parent parent = parentRepository.getByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(parentFirstName, parentLastName);
        String firstPartNameFile = parent.getFirstName() + "_" + parent.getLastName() + "_" + parent.getPet().getName();
        Path filePath = Path.of(avatarsDir, firstPartNameFile + "." + getExtensions(Objects.requireNonNull(photo.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = photo.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        Report report = findReport(parentFirstName, parentLastName);
        report.setFilePath(filePath.toString());
        report.setFileSize(photo.getSize());
        report.setMediaType(photo.getContentType());
        report.setData(photo.getBytes());
        report.setPetDiet(diet);
        report.setPetHealth(health);
        report.setPetBehavior(behavior);
        report.setParent(parent);
        report.setPet(parent.getPet());
        reportRepository.save(report);

        return ResponseEntity.ok().build();
    }

    /**
     * Метод возвращает усыновителя по имени и фамилии
     *
     * @param parentFirstName   <i> является именем усыновителя </i> <br>
     * @param parentLastName<i> <i> является фамилией усыновителя </i>
     */
    private Report findReport(String parentFirstName, String parentLastName) {
        return reportRepository.findByParentFirstNameContainsIgnoreCaseAndParentLastNameContainsIgnoreCase(parentFirstName, parentLastName).orElseGet(Report::new);
    }

    /**
     * Метод создает расширение файла
     *
     * @param fileName <i> является названием файла </i>
     */
    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
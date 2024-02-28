package com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetHealth;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.ReportServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Интерфейс {@link ReportService}
 * содержит набор методов для сервисного класса {@link ReportServiceImpl}
 */
public interface ReportService {

    /**
     * Метод добавляет отчет о животном в БД
     *
     * @param parentFirstName <i> является именем усыновителя </i> <br>
     * @param parentLastName  <i> является фамилией усыновителя </i> <br>
     * @param photo           <i> является отправляемым фото животного </i> <br>
     * @param diet            <i> является описательной частью рациона животного </i> <br>
     * @param health          <i> является описательной частью состояния животного </i> <br>
     * @param behavior        <i> является описательной частью поведения животного </i>
     */
    ResponseEntity<String> uploadReport(String parentFirstName,
                                        String parentLastName,
                                        MultipartFile photo,
                                        String diet,
                                        PetHealth health,
                                        String behavior) throws IOException;
}
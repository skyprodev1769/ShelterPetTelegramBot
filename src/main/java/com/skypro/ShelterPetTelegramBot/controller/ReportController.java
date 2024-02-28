package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetHealth;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@RequestParam String parentFirstName,
                                               @RequestParam String parentLastName,
                                               @RequestParam MultipartFile report,
                                               @RequestParam String diet,
                                               @RequestParam PetHealth health,
                                               @RequestParam String behavior) throws IOException {

        return service.uploadReport(parentFirstName, parentLastName, report, diet, health, behavior);
    }
}
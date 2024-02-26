package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collection;

/**
 * Класс {@link ParentController}
 * является контроллером для обработки запросов, связанных с усыновителем
 */
@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private ParentService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping
    public Parent add(@RequestParam String firstName,
                      @RequestParam String lastName,
                      @RequestParam String phoneNumber,
                      @RequestParam Long volunteerId,
                      @RequestParam Long petId) {

        return service.add(firstName, lastName, phoneNumber, volunteerId, petId);
    }

    @GetMapping("{id}")
    public Parent getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Collection<Parent> getAllByParameters(@RequestParam(required = false) String firstName,
                                                 @RequestParam(required = false) String lastName,
                                                 @RequestParam(required = false) String phoneNumber,
                                                 @RequestParam(required = false) Long volunteerId) {

        return service.getAllByParameters(firstName, lastName, phoneNumber, volunteerId);
    }

    @GetMapping("/all")
    public Collection<Parent> getAll() {
        return service.getAll();
    }

    @PutMapping("{id}")
    public Parent edit(@PathVariable Long id,
                       @RequestParam(required = false) String firstName,
                       @RequestParam(required = false) String lastName,
                       @RequestParam(required = false) String phoneNumber,
                       @RequestParam(required = false) Long volunteerId,
                       @RequestParam(required = false) Long petId) {

        return service.edit(id, firstName, lastName, phoneNumber, volunteerId, petId);
    }

    @DeleteMapping("{id}")
    public Parent delete(@PathVariable Long id) {
        return service.delete(id);
    }
}